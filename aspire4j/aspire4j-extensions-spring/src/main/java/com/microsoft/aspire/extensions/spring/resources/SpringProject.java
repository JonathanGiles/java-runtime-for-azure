package com.microsoft.aspire.extensions.spring.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.extensions.spring.implementation.SpringDeploymentStrategy;
import com.microsoft.aspire.extensions.spring.implementation.SpringIntrospector;
import com.microsoft.aspire.resources.Container;
import com.microsoft.aspire.resources.DockerFile;
import com.microsoft.aspire.resources.ResourceType;
import com.microsoft.aspire.resources.traits.IntrospectiveResource;
import com.microsoft.aspire.resources.traits.ResourceWithTemplate;
import com.microsoft.aspire.utils.FileUtilities;
import com.microsoft.aspire.utils.json.RelativePath;
import com.microsoft.aspire.utils.json.RelativePathSerializer;
import com.microsoft.aspire.utils.templates.TemplateEngine;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpringProject extends Container<SpringProject>
                            implements ResourceWithTemplate<SpringProject>, IntrospectiveResource {    
    private static final ResourceType SPRING_PROJECT = ResourceType.fromString("project.spring.v0");

    @Valid
    @JsonProperty("strategies")
    private Set<SpringDeploymentStrategy> strategies;

    @NotNull(message = "Project.path cannot be null")
    @NotEmpty(message = "Project.path cannot be an empty string")
    @JsonProperty("path")
    @JsonSerialize(using = RelativePathSerializer.class)
    @RelativePath
    private String path;

    @JsonIgnore
    private boolean openTelemetryEnabled;
    
    private String templateDockerfilePath;
    
    public SpringProject(String name) {
        super(SPRING_PROJECT, name, null);
        withEnvironment("spring.application.name", name);

        // establish a convention that a Spring project will have a path that is equal to the name of the project
        withPath(name);
    }

    private SpringProject(String name, ResourceType type) {
        super(type, name, null);
    }

    @Override
    public void onResourcePrecommit() {
        super.onResourcePrecommit();
        introspect();
    }

    @Override
    public void introspect() {
        // we add the available strategies to the aspire manifest and leave it to azd to try its best...
        Map<String, String> outputEnvs = new HashMap<>();
        this.strategies = new SpringIntrospector().introspect(this, outputEnvs);

        // Add the environment introspected from the project
        outputEnvs.forEach((k, v) -> {
            if (!k.startsWith("BUILD_")) {
                withEnvironment(k, v);
            }
        });
        if (outputEnvs.containsKey("SERVER_PORT")) {
            int serverPort = Integer.parseInt(outputEnvs.get("SERVER_PORT"));
            withHttpEndpoint(serverPort);
        }

        if (outputEnvs.containsKey("BUILD_IMAGE")) {
            withImage(outputEnvs.get("BUILD_IMAGE"));
        }
        
        // but, we also look in the strategies to see if we found a dockerfile strategy, as in that case we transform
        // this entire output from a Spring project resource into a dockerfile resource
        strategies.stream()
                  .filter(s -> s.getType() == SpringDeploymentStrategy.DeploymentType.DOCKER_FILE)
                  .findFirst().ifPresent(s -> {
            // we need to set the service name (to the existing spring project name), the path to the Dockerfile, and the
            // context name (which is the directory containing the Dockerfile)
            // FIXME ugly generics
            DockerFile<?> dockerFile = new DockerFile<>(getName());

            String dockerFilePath = s.getCommands().get(0)[0];
            String contextPath = Paths.get(dockerFilePath).getParent().toString();
            this.copyInto(dockerFile);
            dockerFile.withPath(dockerFilePath)
                    .withContext(contextPath)
                    .withExternalHttpEndpoints(); // FIXME this is not really the context

            DistributedApplication.getInstance().substituteResource(this, dockerFile);
        });
        
        // if we need to rebuild the image with more attributes
        if ("true".equals(outputEnvs.get("BUILD_ADD_OTEL_AGENT"))) {
            strategies.stream()
                    .filter(s -> s.getType() == SpringDeploymentStrategy.DeploymentType.MAVEN_POM)
                    .findFirst().ifPresent(s -> {
                        String imageResourceName = getName() + "-image";
                        SpringProject containerImage = new SpringProject(imageResourceName, ResourceType.fromString("project.spring.image.v0"));
                                containerImage.strategies = strategies;
                                containerImage.withImage(outputEnvs.get("BUILD_IMAGE"))
                                        .withPath(this.getPath());
                                DistributedApplication.getInstance().addResource(containerImage);
                                
                                DockerFile<?> dockerFile = new DockerFile<>(getName());
                                String dockerFilePath = this.templateDockerfilePath;
                                String contextPath = Paths.get(dockerFilePath).getParent().toString();
                                this.copyInto(dockerFile);
                                dockerFile.withPath(dockerFilePath)
                                        .withContext(contextPath)
                                        .withExternalHttpEndpoints()
                                        .withBuildArg("BASE_IMAGE", "${%s.image}".formatted(imageResourceName))
                                ; // FIXME this is not really the context

                                DistributedApplication.getInstance().substituteResource(this, dockerFile);
                            }
                    );
        }
    }

    /**
     * The path to the project file. Relative paths are interpreted as being relative to the location of the manifest file.
     * @param path
     * @return
     */
    @JsonIgnore
    public SpringProject withPath(String path) {
        this.path = path;
        return self();
    }

    @JsonIgnore
    public final String getPath() {
        return path;
    }

    @JsonIgnore
    public SpringProject withOpenTelemetry() {
        this.openTelemetryEnabled = true;
        return self();
    }

    @JsonIgnore
    public boolean isOpenTelemetryEnabled() {
        return openTelemetryEnabled;
    }

    @Override
    public SpringProject self() {
        return this;
    }

    @Override
    public List<TemplateFileOutput> processTemplate() {
        if (openTelemetryEnabled) {
            Map<String, Object> properties = new HashMap<>();
            properties.put("BASE_IMAGE", "${BASE_IMAGE}");
            final String templatePath = "/templates/opentelemetry/";
            final String outputRootPath = "opentelemetry/";
            List<TemplateDescriptor> templateFiles = TemplateDescriptorsBuilder.begin(templatePath, outputRootPath)
                    .with("Dockerfile")
                    .with("JAVA_TOOL_OPTIONS.append")
                    .with("JAVA_TOOL_OPTIONS.delim")
                    .build();

            List<TemplateFileOutput> templateOutput = TemplateEngine.process(SpringProject.class, templateFiles, properties);

            // Important - as noted in the javadoc - from the perspective of the API below, the paths are relative to the
            // directory in which azd is running, NOT the output directory. These paths will then be transformed at
            // serialization time to be relative to the output directory.
            // This is slightly unfortunate, as we know the correct directory here, but we don't have a way to pass it.
            templateDockerfilePath = FileUtilities.convertOutputPathToRootRelative(outputRootPath + "Dockerfile").toString();
            
            return templateOutput;
        }
        return List.of();
    }
}

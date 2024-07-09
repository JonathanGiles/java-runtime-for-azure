package com.microsoft.aspire.extensions.spring.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.extensions.spring.implementation.SpringDeploymentStrategy;
import com.microsoft.aspire.extensions.spring.implementation.SpringIntrospector;
import com.microsoft.aspire.implementation.json.RelativePath;
import com.microsoft.aspire.implementation.json.RelativePathSerializer;
import com.microsoft.aspire.resources.Container;
import com.microsoft.aspire.resources.DockerFile;
import com.microsoft.aspire.resources.ResourceType;
import com.microsoft.aspire.resources.traits.IntrospectiveResource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SpringProject extends Container<SpringProject> implements IntrospectiveResource {
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

    public SpringProject(String name) {
        super(SPRING_PROJECT, name, null);
        withEnvironment("spring.application.name", name);
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

            String dockerFilePath = s.getCommands().get(0);
            String contextPath = Paths.get(dockerFilePath).getParent().toString();
            this.copyInto(dockerFile);
            dockerFile.withPath(dockerFilePath)
                    .withContext(contextPath)
                    .withExternalHttpEndpoints(); // FIXME this is not really the context

            DistributedApplication.getInstance().substituteResource(this, dockerFile);
        });
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

    @Override
    public SpringProject self() {
        return this;
    }
}

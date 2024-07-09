package com.microsoft.aspire.extensions.spring.resources;

import com.microsoft.aspire.resources.DockerFile;
import com.microsoft.aspire.resources.traits.ResourceWithEndpoints;
import com.microsoft.aspire.resources.traits.ResourceWithTemplate;
import com.microsoft.aspire.utils.templates.TemplateEngine;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EurekaServiceDiscovery extends DockerFile<EurekaServiceDiscovery>
                                          implements ResourceWithTemplate<EurekaServiceDiscovery>,
                                                     ResourceWithEndpoints<EurekaServiceDiscovery> {

    private final String PROPERTY_NAME = "name";
    private final String PROPERTY_TARGET_PORT = "targetPort";
    private final String PROPERTY_REGISTER_WITH_EUREKA = "registerWithEureka";
    private final String PROPERTY_FETCH_REGISTRY = "fetchRegistry";

    private static final int DEFAULT_TARGET_PORT = 8761;

    private final Map<String, Object> properties;

    public EurekaServiceDiscovery(String name) {
        super(name);

        this.properties = new HashMap<>();
        this.properties.put(PROPERTY_NAME, name);
        this.properties.put(PROPERTY_TARGET_PORT, DEFAULT_TARGET_PORT);
        this.properties.put(PROPERTY_REGISTER_WITH_EUREKA, false);
        this.properties.put(PROPERTY_FETCH_REGISTRY, false);

        withPort(DEFAULT_TARGET_PORT);
    }

    public EurekaServiceDiscovery withPort(int targetPort) {
        this.properties.put(PROPERTY_TARGET_PORT, targetPort);

        withHttpEndpoint(targetPort);
        withHttpsEndpoint(targetPort);

        return this;
    }

    @Override
    public List<TemplateFileOutput> processTemplate(Path outputPath) {
        final String templatePath = "/templates/eureka/";
        final String outputRootPath = "eureka/";
        List<TemplateDescriptor> templateFiles = TemplateDescriptorsBuilder.begin(templatePath, outputRootPath)
            .with("pom.xml")
            .with("Dockerfile")
            .with("EurekaServerApplication.java", "src/main/java/com/microsoft/aspire/spring/eureka/EurekaServerApplication.java")
            .with("application.yaml", "src/main/resources/application.yaml")
            .build();

        List<TemplateFileOutput> templateOutput = TemplateEngine.process(EurekaServiceDiscovery.class, templateFiles, properties);

        // FIXME we need a better way of determining the output path of the template
        withPath("output/" + outputRootPath + "Dockerfile");
        withContext("output/" + outputRootPath);

        return templateOutput;
    }

    @Override
    public EurekaServiceDiscovery self() {
        return this;
    }
}

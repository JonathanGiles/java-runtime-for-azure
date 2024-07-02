package com.microsoft.aspire.extensions.spring.resources;

import com.microsoft.aspire.resources.DockerFile;
import com.microsoft.aspire.resources.traits.ResourceWithTemplate;
import com.microsoft.aspire.utils.templates.TemplateEngine;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EurekaServiceDiscovery extends DockerFile<EurekaServiceDiscovery>
                                          implements ResourceWithTemplate<EurekaServiceDiscovery> {

    private final String PROPERTY_NAME = "name";
    private final String PROPERTY_PORT = "port";
    private final String PROPERTY_REGISTER_WITH_EUREKA = "registerWithEureka";
    private final String PROPERTY_FETCH_REGISTRY = "fetchRegistry";

    private final Map<String, Object> properties;

    public EurekaServiceDiscovery(String name) {
        super(name);

        this.properties = new HashMap<>();
        this.properties.put(PROPERTY_NAME, name);
        this.properties.put(PROPERTY_PORT, 8761);
        this.properties.put(PROPERTY_REGISTER_WITH_EUREKA, false);
        this.properties.put(PROPERTY_FETCH_REGISTRY, false);
    }

    public EurekaServiceDiscovery withPort(int port) {
        this.properties.put(PROPERTY_PORT, port);
        return this;
    }

    @Override
    public List<TemplateFileOutput> processTemplate(Path outputPath) {
        final String inPrefix = "/templates/eureka/";
        final String outPrefix = "eureka/";
        List<TemplateDescriptor> templateFiles = List.of(
            new TemplateDescriptor(inPrefix+"pom.xml", outPrefix+"pom.xml"),
            new TemplateDescriptor(inPrefix+"Dockerfile", outPrefix+"Dockerfile"),
            new TemplateDescriptor(inPrefix+"EurekaServerApplication.java", outPrefix+"src/main/java/com/microsoft/aspire/spring/eureka/EurekaServerApplication.java"),
            new TemplateDescriptor(inPrefix+"application.yaml", outPrefix+"src/main/resources/application.yaml")
        );

        List<TemplateFileOutput> templateOutput = TemplateEngine.process(EurekaServiceDiscovery.class, templateFiles, properties);

        // TODO we know that we need to get the output filename from the first element, and set that as the path
        withPath(outputPath.resolve(templateOutput.get(0).filename()).toString());
        withContext(outputPath.resolve(templateOutput.get(0).filename()).getParent().toString());

        return templateOutput;
    }

    @Override
    public EurekaServiceDiscovery self() {
        return this;
    }
}

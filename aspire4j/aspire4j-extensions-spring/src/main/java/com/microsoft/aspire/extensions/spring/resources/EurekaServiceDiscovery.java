package com.microsoft.aspire.extensions.spring.resources;

import com.microsoft.aspire.resources.DockerFile;
import com.microsoft.aspire.resources.ResourceType;
import com.microsoft.aspire.resources.traits.ResourceWithTemplate;

import java.util.List;

public class EurekaServiceDiscovery extends DockerFile<EurekaServiceDiscovery>
                                    implements ResourceWithTemplate<EurekaServiceDiscovery> {

    public EurekaServiceDiscovery(String name) {
        super(ResourceType.fromString("spring.eureka.server.v0"), name);

        // TODO
        // We have a template for the eureka server, consisting of a minimal Spring Boot application, as well as
        // pom.xml, configuration file, and a Dockerfile to build the image. We need to allow for the template properties
        // to be set by the user, such as the port, the name of the service, etc, and then we need to write these out
        // to a temporary location, and then build the image from that location.
    }

    @Override
    public List<TemplateFileOutput> processTemplate() {
        return List.of();
    }

    @Override
    public EurekaServiceDiscovery self() {
        return this;
    }
}

package com.azure.runtime.host.extensions.spring.resources;

import com.azure.runtime.host.extensions.microservice.common.resources.MicroserviceProject;
import com.azure.runtime.host.resources.ResourceType;
import com.azure.runtime.host.resources.traits.IntrospectiveResource;
import com.azure.runtime.host.resources.traits.ResourceWithTemplate;

public class SpringProject extends MicroserviceProject<SpringProject>
                            implements ResourceWithTemplate<SpringProject>, IntrospectiveResource {    
    private static final ResourceType SPRING_PROJECT = ResourceType.fromString("project.spring.v0");

    public SpringProject(String name) {
        this(SPRING_PROJECT, name);
    }

    private SpringProject(ResourceType type, String name) {
        super(type, name);
        withEnvironment("spring.application.name", name);
    }
}

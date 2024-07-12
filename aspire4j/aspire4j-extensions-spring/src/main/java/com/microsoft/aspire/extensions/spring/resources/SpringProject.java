package com.microsoft.aspire.extensions.spring.resources;

import com.microsoft.aspire.extensions.microservice.common.resources.MicroserviceProject;
import com.microsoft.aspire.resources.ResourceType;
import com.microsoft.aspire.resources.traits.IntrospectiveResource;
import com.microsoft.aspire.resources.traits.ResourceWithTemplate;

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

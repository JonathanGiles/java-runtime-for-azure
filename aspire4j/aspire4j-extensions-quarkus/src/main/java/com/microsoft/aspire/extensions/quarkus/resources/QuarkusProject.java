package com.microsoft.aspire.extensions.quarkus.resources;

import com.microsoft.aspire.extensions.microservice.common.resources.MicroserviceProject;
import com.microsoft.aspire.resources.ResourceType;
import com.microsoft.aspire.resources.traits.IntrospectiveResource;
import com.microsoft.aspire.resources.traits.ResourceWithTemplate;

public class QuarkusProject extends MicroserviceProject<QuarkusProject>
                            implements ResourceWithTemplate<QuarkusProject>, IntrospectiveResource {
    // FIXME we should introduce a standard name for all microservice frameworks, as azd can treat them the same
    private static final ResourceType QUARKUS_PROJECT = ResourceType.fromString("project.quarkus.v0");

    public QuarkusProject(String name) {
        this(QUARKUS_PROJECT, name);
    }

    private QuarkusProject(ResourceType type, String name) {
        super(type, name);

        // FIXME
//        withEnvironment("spring.application.name", name);
    }
}

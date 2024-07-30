package com.azure.runtime.host.extensions.micronaut.resources;

import com.azure.runtime.host.extensions.microservice.common.resources.MicroserviceProject;
import com.azure.runtime.host.resources.ResourceType;
import com.azure.runtime.host.resources.traits.IntrospectiveResource;
import com.azure.runtime.host.resources.traits.ResourceWithTemplate;

public class MicronautProject extends MicroserviceProject<MicronautProject>
        implements ResourceWithTemplate<MicronautProject>, IntrospectiveResource {
    // FIXME we should introduce a standard name for all microservice frameworks, as azd can treat them the same
    private static final ResourceType MICRONAUT_PROJECT = ResourceType.fromString("project.micronaut.v0");

    public MicronautProject(String name) {
        this(MICRONAUT_PROJECT, name);
    }

    private MicronautProject(ResourceType type, String name) {
        super(type, name);

        // FIXME
//        withEnvironment("spring.application.name", name);
    }
}

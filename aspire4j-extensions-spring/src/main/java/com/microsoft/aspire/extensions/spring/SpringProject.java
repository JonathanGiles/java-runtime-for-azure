package com.microsoft.aspire.extensions.spring;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.aspire.extensions.spring.implementation.SpringIntrospector;
import com.microsoft.aspire.resources.Project;
import com.microsoft.aspire.resources.ResourceType;
import com.microsoft.aspire.resources.traits.IntrospectiveResource;
import jakarta.validation.Valid;

import java.util.LinkedHashMap;
import java.util.Map;

public class SpringProject extends Project implements IntrospectiveResource {
    private static final ResourceType SPRING_PROJECT = ResourceType.fromString("project.spring.v0");

    @Valid
    @JsonProperty("properties")
    private final Map<String, String> properties;

    public SpringProject(String name) {
        super(SPRING_PROJECT, name);

        this.properties = new LinkedHashMap<>();
    }

    public SpringProject withOpenTelemetryAgent() {
        // TODO
        properties.put("openTelemetryAgent", "true");
        return this;
    }

    @Override
    public void introspect() {
        // TODO we need to know if the type should change. Also, problem: we kind of want to change the resource entirely
        // rather than just change its type. This probably needs a rethink to some form of substitution approach, where
        // we say to the apphost that we want to remove ourselves, and replace ourselves with a different resource.
        properties.putAll(new SpringIntrospector().introspect(this));
    }
}

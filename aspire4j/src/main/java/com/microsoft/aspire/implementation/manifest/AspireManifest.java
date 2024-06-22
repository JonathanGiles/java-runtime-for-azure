package com.microsoft.aspire.implementation.manifest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.aspire.resources.Resource;
import jakarta.validation.Valid;

import java.util.*;

public class AspireManifest {
    @JsonProperty("$schema")
    private final String schema = "https://json.schemastore.org/aspire-8.0.json";

    // Map from resource name to resource
    @Valid
    @JsonProperty("resources")
    private final Map<String, Resource> resources;

    public AspireManifest() {
        this.resources = new LinkedHashMap<>();
    }

    public <T extends Resource> T addResource(T resource) {
        Objects.requireNonNull(resource);
        resources.put(resource.getName(), resource);
        return resource;
    }

    public boolean isEmpty() {
        return resources.isEmpty();
    }
}

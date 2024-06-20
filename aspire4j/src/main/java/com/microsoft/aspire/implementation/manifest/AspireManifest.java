package com.microsoft.aspire.implementation.manifest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.aspire.components.azure.AzureStorageResource;
import com.microsoft.aspire.components.common.Resource;

import java.util.*;

public class AspireManifest {
    @JsonProperty("$schema")
    private final String schema = "https://json.schemastore.org/aspire-8.0.json";

    // Map from resource name to resource
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
}

package com.azure.runtime.host;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.azure.runtime.host.resources.Resource;
import jakarta.validation.Valid;

import java.util.*;

// Not public API
class JavaAppHostManifest {
    @JsonProperty("$schema")
    private final String schema = "https://json.schemastore.org/aspire-8.0.json";

    // Map from resource name to resource
    @Valid
    @JsonProperty("resources")
    Map<String, Resource<?>> resources;

    JavaAppHostManifest() {
        this.resources = new LinkedHashMap<>();
    }

    <T extends Resource<?>> T addResource(T resource) {
        Objects.requireNonNull(resource);

        if (resources.containsKey(resource.getName())) {
            throw new IllegalArgumentException("Resource with name " + resource.getName() + " already exists in manifest");
        }

        resources.put(resource.getName(), resource);
        resource.onResourceAdded();
        return resource;
    }

    <T extends Resource<?>> T removeResource(T resource) {
        Objects.requireNonNull(resource);
        resources.remove(resource.getName());
        resource.onResourceRemoved();
        return resource;
    }

    Map<String, Resource<?>> getResources() {
        return resources;
    }

    boolean isEmpty() {
        return resources.isEmpty();
    }

    public void substituteResource(Resource<?> oldResource, Resource<?>... newResources) {
        if (oldResource == null) {
            throw new IllegalArgumentException("oldResource cannot be null");
        }
        if (newResources == null || newResources.length == 0) {
            throw new IllegalArgumentException("newResources cannot be null or empty");
        }
        if (!resources.containsKey(oldResource.getName())) {
            throw new IllegalArgumentException("oldResource not found in manifest");
        }
        for (Resource<?> newResource : newResources) {
            if (resources.containsKey(newResource.getName()) && !newResource.getName().equals(oldResource.getName())) {
                throw new IllegalArgumentException("newResource already exists in manifest");
            }
        }

        Map<String, Resource<?>> newResourcesMap = new LinkedHashMap<>();
        for (Map.Entry<String, Resource<?>> entry : resources.entrySet()) {
            if (entry.getKey().equals(oldResource.getName())) {
                for (Resource<?> newResource : newResources) {
                    newResourcesMap.put(newResource.getName(), newResource);
                }
            } else {
                newResourcesMap.put(entry.getKey(), entry.getValue());
            }
        }
        resources = newResourcesMap;
        oldResource.onResourceRemoved();
    }
}

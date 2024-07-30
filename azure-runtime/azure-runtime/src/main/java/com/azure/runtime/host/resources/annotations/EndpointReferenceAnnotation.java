package com.azure.runtime.host.resources.annotations;

import com.azure.runtime.host.resources.Resource;

import java.util.HashSet;
import java.util.Objects;

public class EndpointReferenceAnnotation<T extends Resource<?>> implements ResourceAnnotation {
    private final T resource;
    private boolean useAllEndpoints;
    private final HashSet<String> endpointNames;

    public EndpointReferenceAnnotation(T resource) {
        this.resource = Objects.requireNonNull(resource, "resource cannot be null");
        this.endpointNames = new HashSet<>();
    }

    public T getResource() {
        return resource;
    }

    public boolean isUseAllEndpoints() {
        return useAllEndpoints;
    }

    public void setUseAllEndpoints(boolean useAllEndpoints) {
        this.useAllEndpoints = useAllEndpoints;
    }

    public HashSet<String> getEndpointNames() {
        return endpointNames;
    }

    @Override
    public String toString() {
        return "EndpointReferenceAnnotation{" +
            "resource=" + resource.getName() +
            ", useAllEndpoints=" + useAllEndpoints +
            ", endpointNames=" + endpointNames +
            '}';
    }
}
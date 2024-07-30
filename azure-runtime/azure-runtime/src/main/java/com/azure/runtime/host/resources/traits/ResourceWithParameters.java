package com.azure.runtime.host.resources.traits;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.azure.runtime.host.resources.Resource;

import java.util.Map;

/**
 * Represents a resource that can have parameters.
 */
public interface ResourceWithParameters<T extends Resource<T> & ResourceWithParameters<T>> extends ResourceTrait<T> {

    /**
     * Adds a parameter to this resource.
     */
    T withParameter(String key, Object value);

    /**
     * Returns a read-only map of parameters for this resource.
     */
    @JsonIgnore
    Map<String, Object> getParameters();

    default T withParameters(Map<String, Object> parameters) {
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            withParameter(entry.getKey(), entry.getValue());
        }
        return self();
    }
}

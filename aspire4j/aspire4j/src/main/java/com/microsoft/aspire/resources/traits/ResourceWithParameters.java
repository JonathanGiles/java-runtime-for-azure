package com.microsoft.aspire.resources.traits;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public interface ResourceWithParameters<T extends ResourceWithParameters<T>> extends SelfAware<T> {

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

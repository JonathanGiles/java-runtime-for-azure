package com.microsoft.aspire.resources.traits;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public interface ResourceWithParameters<T extends ResourceWithParameters<T>> {

    T withParameter(String key, String value);

    /**
     * Returns a read-only map of parameters for this resource.
     */
    @JsonIgnore
    Map<String, String> getParameters();

    default T withParameters(Map<String, String> parameters) {
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            withParameter(entry.getKey(), entry.getValue());
        }
        return (T) this;
    }
}

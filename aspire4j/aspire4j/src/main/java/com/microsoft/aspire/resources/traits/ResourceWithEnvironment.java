package com.microsoft.aspire.resources.traits;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public interface ResourceWithEnvironment<T extends ResourceWithEnvironment<T>> extends ResourceWithReference<T> {

    T withEnvironment(String key, String value);

    /**
     * Returns an unmodifiable view of the environment variables.
     */
    @JsonIgnore
    Map<String, String> getEnvironment();

    default T withEnvironment(Map<String, String> environment) {
        for (Map.Entry<String, String> entry : environment.entrySet()) {
            withEnvironment(entry.getKey(), entry.getValue());
        }
        return self();
    }

    @Override
    T self();
}

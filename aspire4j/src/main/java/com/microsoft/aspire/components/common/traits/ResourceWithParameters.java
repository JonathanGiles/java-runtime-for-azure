package com.microsoft.aspire.components.common.traits;

import java.util.Map;

public interface ResourceWithParameters<T extends ResourceWithParameters<T>> {

    T withParameter(String key, String value);

    default T withParameters(Map<String, String> parameters) {
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            withParameter(entry.getKey(), entry.getValue());
        }
        return (T) this;
    }
}

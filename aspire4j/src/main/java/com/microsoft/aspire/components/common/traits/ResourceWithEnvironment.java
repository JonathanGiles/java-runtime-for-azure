package com.microsoft.aspire.components.common.traits;

import com.microsoft.aspire.components.common.Resource;

import java.util.Map;

public interface ResourceWithEnvironment<T extends ResourceWithEnvironment<T>> {

    T withEnvironment(String key, String value);

    default T withEnvironment(Map<String, String> environment) {
        for (Map.Entry<String, String> entry : environment.entrySet()) {
            withEnvironment(entry.getKey(), entry.getValue());
        }
        return (T) this;
    }

    default T withReference(Resource resource) {
        // FIXME! Somehow we need to know what the reference is
        withEnvironment("ConnectionStrings__blobs", "{blobs.connectionString}");
        return (T) this;
    }
}

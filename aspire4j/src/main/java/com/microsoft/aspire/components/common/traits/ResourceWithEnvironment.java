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

    default T withReference(ResourceWithConnectionString resource) {
        // FIXME! Somehow we need to know what the reference is
        // https://learn.microsoft.com/en-us/dotnet/api/aspire.hosting.resourcebuilderextensions.withreference?view=dotnet-aspire-8.0.1#aspire-hosting-resourcebuilderextensions-withreference-1(aspire-hosting-applicationmodel-iresourcebuilder((-0))-aspire-hosting-applicationmodel-iresourcebuilder((aspire-hosting-applicationmodel-iresourcewithconnectionstring))-system-string-system-boolean)
        withEnvironment("ConnectionStrings__blobs", "{blobs.connectionString}");
        return (T) this;
    }
}

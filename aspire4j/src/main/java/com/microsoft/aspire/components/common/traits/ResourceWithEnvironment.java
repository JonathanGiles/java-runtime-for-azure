package com.microsoft.aspire.components.common.traits;

public interface ResourceWithEnvironment<T extends ResourceWithEnvironment<T>> {

    T withEnvironment(String key, String value);
}

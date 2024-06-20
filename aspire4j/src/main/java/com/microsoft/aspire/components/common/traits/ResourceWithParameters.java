package com.microsoft.aspire.components.common.traits;

public interface ResourceWithParameters<T extends ResourceWithParameters<T>> {

    T withParameter(String key, String value);
}

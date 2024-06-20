package com.microsoft.aspire.components.common.traits;

public interface ResourceWithArguments<T extends ResourceWithArguments<T>> {

    T withArgument(String argument);
}

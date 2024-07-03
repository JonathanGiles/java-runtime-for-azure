package com.microsoft.aspire.resources.traits;

import com.fasterxml.jackson.annotation.JsonIgnore;

// Refer to https://learn.microsoft.com/en-us/dotnet/aspire/fundamentals/persist-data-volumes for more info
public interface ResourceWithDataVolume<T extends ResourceWithDataVolume<T>> extends SelfAware<T> {

    @JsonIgnore
    T withDataVolume();
}

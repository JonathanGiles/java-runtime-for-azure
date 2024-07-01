package com.microsoft.aspire.resources.traits;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microsoft.aspire.resources.properties.EndpointReference;

import java.util.List;

public interface ResourceWithEndpoints<T extends ResourceWithEndpoints<T>> {

    @JsonIgnore
    List<EndpointReference> getEndpoints();
}

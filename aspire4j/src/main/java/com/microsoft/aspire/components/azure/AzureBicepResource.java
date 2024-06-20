package com.microsoft.aspire.components.azure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.aspire.components.common.Resource;
import com.microsoft.aspire.components.common.traits.ResourceWithParameters;

import java.util.LinkedHashMap;
import java.util.Map;

public class AzureBicepResource extends Resource implements ResourceWithParameters<AzureBicepResource> {

    @JsonProperty("params")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> parameters = new LinkedHashMap<>();

    public AzureBicepResource(String type, String name) {
        super(type, name);
    }

    public AzureBicepResource withParameter(String name, String value) {
        parameters.put(name, value);
        return this;
    }
}

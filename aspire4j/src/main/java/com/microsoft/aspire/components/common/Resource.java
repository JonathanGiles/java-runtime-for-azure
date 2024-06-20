package com.microsoft.aspire.components.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"type", "params"})
public abstract class Resource {
    @JsonProperty("type")
    private final String type;

    // the name of the resource
    @JsonIgnore
    private final String name;

    public Resource(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}

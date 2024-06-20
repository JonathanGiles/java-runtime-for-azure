package com.microsoft.aspire.components.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonPropertyOrder({"type", "params"})
public abstract class Resource {
    @NotNull(message = "type cannot be null")
    @NotEmpty(message = "type cannot be an empty string")
    @JsonProperty("type")
    private final String type;

    // the name of the resource
    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be an empty string")
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

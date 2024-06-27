package com.microsoft.aspire.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonPropertyOrder({"type", "params"})
public abstract class Resource {

    @Valid
    @NotNull(message = "Resource Type cannot be null")
    @JsonProperty("type")
    private final ResourceType type;

    // the name of the resource
    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be an empty string")
    @JsonIgnore
    private final String name;

    public Resource(ResourceType type, String name) {
        this.type = type;
        this.name = name;
    }

    public final ResourceType getType() {
        return type;
    }

    public final String getName() {
        return name;
    }
}

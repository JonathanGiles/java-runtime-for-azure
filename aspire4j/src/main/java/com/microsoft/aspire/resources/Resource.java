package com.microsoft.aspire.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonPropertyOrder({"type", "params"})
public abstract class Resource {

    public enum ResourceType {
        AZURE_BICEP("azure.bicep.v0"),
        CONTAINER("container.v0"),
        DOCKER_FILE("dockerfile.v0"),
        EXECUTABLE("executable.v0"),
        PROJECT("project.v0"),
        VALUE("value.v0");

        final String type;

        ResourceType(String type) {
            this.type = type;
        }

        @JsonValue
        public String toString() {
            return type;
        }
    }


    @NotNull(message = "type cannot be null")
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

    public ResourceType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}

package com.microsoft.aspire.resources;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ResourceType {
    public static final ResourceType AZURE_BICEP = fromString("azure.bicep.v0");
    public static final ResourceType CONTAINER = fromString("container.v0");
    public static final ResourceType DOCKER_FILE = fromString("dockerfile.v0");
    public static final ResourceType EXECUTABLE = fromString("executable.v0");
    public static final ResourceType PROJECT = fromString("project.v0");
    public static final ResourceType VALUE = fromString("value.v0");

    @NotNull(message = "Resource type cannot be null")
    @NotEmpty(message = "Resource type cannot be empty")
    private final String value;

    private ResourceType(String value) {
        this.value = value;
    }

    public static ResourceType fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Resource type cannot be null");
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Resource type cannot be empty");
        }
        return new ResourceType(value);
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}

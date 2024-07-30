package com.azure.runtime.host.resources;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * An expandable enum-like type for specifying the type of a resource. This is used to determine how to process the
 * resource when being processed by tools such as azd.
 */
public class ResourceType {
    /**
     * An Azure Bicep file - consider using {@link AzureBicepResource}.
     */
    public static final ResourceType AZURE_BICEP = fromString("azure.bicep.v0");

    /**
     * A container resource - consider using {@link Container}.
     */
    public static final ResourceType CONTAINER = fromString("container.v0");

    /**
     * A Dockerfile resource - consider using {@link DockerFile}.
     */
    public static final ResourceType DOCKER_FILE = fromString("dockerfile.v0");

    /**
     * An executable resource - consider using {@link Executable}.
     */
    public static final ResourceType EXECUTABLE = fromString("executable.v0");

    /**
     * A value resource - consider using {@link Value}.
     */
    public static final ResourceType VALUE = fromString("value.v0");

    @NotNull(message = "Resource type cannot be null")
    @NotEmpty(message = "Resource type cannot be empty")
    private final String value;

    private ResourceType(String value) {
        this.value = value;
    }

    /**
     * Creates a new ResourceType with the given value as the type.
     * @param value The value of the new ResourceType.
     * @return A new ResourceType with the given value as the type.
     */
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

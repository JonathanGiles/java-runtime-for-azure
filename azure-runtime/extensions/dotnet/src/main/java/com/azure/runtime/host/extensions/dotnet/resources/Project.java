package com.azure.runtime.host.extensions.dotnet.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.azure.runtime.host.resources.Resource;
import com.azure.runtime.host.resources.ResourceType;
import com.azure.runtime.host.resources.traits.ResourceWithReference;
import com.azure.runtime.host.utils.json.RelativePath;
import com.azure.runtime.host.resources.traits.ResourceWithArguments;
import com.azure.runtime.host.resources.traits.ResourceWithEndpoints;
import com.azure.runtime.host.resources.traits.ResourceWithEnvironment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/*
{
    "type": "object",
    "description": "Represents a .NET project resource.",
    "required": [
        "type",
        "path"
    ],
    "properties": {
        "type": {
            "const": "project.v0"
        },
        "path": {
            "type": "string",
            "description": "The path to the project file. Relative paths are interpreted as being relative to the location of the manifest file."
        },
        "args": {
            "$ref": "#/definitions/args"
        },
        "env": {
            "$ref": "#/definitions/env"
        },
        "bindings": {
            "$ref": "#/definitions/bindings"
        }
    },
    "additionalProperties": false
},
 */

/**
 * Represents a .NET project resource.
 * @param <T>
 */
@JsonPropertyOrder({"type", "path", "args", "env", "bindings"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Project<T extends Project<T>> extends Resource<T>
           implements ResourceWithArguments<T>, ResourceWithEnvironment<T>, ResourceWithEndpoints<T>, ResourceWithReference<T> {
    public static final ResourceType PROJECT = ResourceType.fromString("project.v0");

    @NotNull(message = "Project.path cannot be null")
    @NotEmpty(message = "Project.path cannot be an empty string")
    @JsonProperty("path")
    @RelativePath
    private String path;

    public Project(String name) {
        this(PROJECT, name);
    }

    protected Project(ResourceType type, String name) {
        super(type, name);
    }

    /**
     * The path to the project file. Relative paths are interpreted as being relative to the location of the manifest file.
     * @param path
     * @return
     */
    @JsonIgnore
    public T withPath(String path) {
        this.path = path;
        return self();
    }

    @JsonIgnore
    public final String getPath() {
        return path;
    }

    @Override
    public T self() {
        return (T) this;
    }
}

package com.microsoft.aspire.extensions.dotnet.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microsoft.aspire.resources.Resource;
import com.microsoft.aspire.resources.ResourceType;
import com.microsoft.aspire.utils.json.RelativePath;
import com.microsoft.aspire.resources.traits.ResourceWithArguments;
import com.microsoft.aspire.resources.traits.ResourceWithEndpoints;
import com.microsoft.aspire.resources.traits.ResourceWithEnvironment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.*;

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
                                           implements ResourceWithArguments<T>,
                                                      ResourceWithEnvironment<T>,
                                                      ResourceWithEndpoints<T> {

    @NotNull(message = "Project.path cannot be null")
    @NotEmpty(message = "Project.path cannot be an empty string")
    @JsonProperty("path")
    @RelativePath
    private String path;

    @JsonProperty("args")
    @Valid
    private final List<String> arguments = new ArrayList<>();

    public Project(String name) {
        this(ResourceType.PROJECT, name);
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

    @Override
    @JsonIgnore
    public T withArgument(String argument) {
        arguments.add(argument);
        return self();
    }

    @JsonIgnore
    public final String getPath() {
        return path;
    }

    @Override
    @JsonIgnore
    public final List<String> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public T self() {
        return (T) this;
    }
}

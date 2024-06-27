package com.microsoft.aspire.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microsoft.aspire.implementation.json.RelativePath;
import com.microsoft.aspire.implementation.json.RelativePathSerializer;
import com.microsoft.aspire.resources.properties.Binding;
import com.microsoft.aspire.resources.traits.ResourceWithArguments;
import com.microsoft.aspire.resources.traits.ResourceWithBindings;
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
@JsonPropertyOrder({"type", "path", "args", "env", "bindings"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Project extends Resource implements ResourceWithArguments<Project>,
                                                 ResourceWithEnvironment<Project>,
                                                 ResourceWithBindings<Project> {

    @NotNull(message = "Project.path cannot be null")
    @NotEmpty(message = "Project.path cannot be an empty string")
    @JsonProperty("path")
    @JsonSerialize(using = RelativePathSerializer.class)
    @RelativePath
    private String path;

    @JsonProperty("env")
    @Valid
    private final Map<String, String> env = new LinkedHashMap<>();

    @JsonProperty("args")
    @Valid
    private final List<String> arguments = new ArrayList<>();

    @JsonProperty("bindings")
    @Valid
    private final Map<Binding.Scheme, Binding> bindings = new LinkedHashMap<>();

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
    public Project withPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    @JsonIgnore
    public Project withEnvironment(String key, String value) {
        this.env.put(key, value);
        return this;
    }

    @Override
    @JsonIgnore
    public Project withArgument(String argument) {
        arguments.add(argument);
        return this;
    }

    @Override
    @JsonIgnore
    public Project withBinding(Binding binding) {
        bindings.put(binding.getScheme(), binding);
        return this;
    }

    @Override
    @JsonIgnore
    public @Valid Map<Binding.Scheme, Binding> getBindings() {
        return Collections.unmodifiableMap(bindings);
    }

    @JsonIgnore
    public final String getPath() {
        return path;
    }

    @Override
    @JsonIgnore
    public final Map<String, String> getEnvironment() {
        return Collections.unmodifiableMap(env);
    }

    @Override
    @JsonIgnore
    public final List<String> getArguments() {
        return Collections.unmodifiableList(arguments);
    }
}

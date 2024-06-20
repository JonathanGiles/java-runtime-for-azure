package com.microsoft.aspire.components.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.aspire.components.common.properties.Binding;
import com.microsoft.aspire.components.common.traits.ResourceWithArguments;
import com.microsoft.aspire.components.common.traits.ResourceWithBindings;
import com.microsoft.aspire.components.common.traits.ResourceWithEnvironment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class Project extends Resource implements ResourceWithArguments<Project>,
                                                       ResourceWithEnvironment<Project>,
                                                       ResourceWithBindings<Project> {

    @NotNull(message = "Project.path cannot be null")
    @NotEmpty(message = "Project.path cannot be an empty string")
    @JsonProperty("path")
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
        super("project.v0", name);
    }

    /**
     * The path to the project file. Relative paths are interpreted as being relative to the location of the manifest file.
     * @param path
     * @return
     */
    public Project withPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public Project withEnvironment(String key, String value) {
        this.env.put(key, value);
        return this;
    }

    public Project withReference(Resource resource) {
        // TODO
        // https://learn.microsoft.com/en-us/dotnet/api/aspire.hosting.resourcebuilderextensions.withreference?view=dotnet-aspire-8.0.1#aspire-hosting-resourcebuilderextensions-withreference-1(aspire-hosting-applicationmodel-iresourcebuilder((-0))-aspire-hosting-applicationmodel-iresourcebuilder((aspire-hosting-applicationmodel-iresourcewithconnectionstring))-system-string-system-boolean)
        return this;
    }

    @Override
    public Project withArgument(String argument) {
        arguments.add(argument);
        return this;
    }

    @Override
    public Project withBinding(Binding binding) {
        bindings.put(binding.getScheme(), binding);
        return this;
    }
}

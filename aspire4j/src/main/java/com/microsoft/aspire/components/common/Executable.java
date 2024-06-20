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

import java.util.*;

/*
{
    "type": "object",
    "description": "Represents an executable resource.",
    "required": [
        "type",
        "command",
        "workingDirectory"
    ],
    "properties": {
        "type": {
            "const": "executable.v0"
        },
        "workingDirectory": {
            "type": "string",
            "description": "The path to the working directory. Should be intepretted as being relative to the AppHost directory."
        },
        "command": {
            "type": "string",
            "description": "The path to the command. Should be interpreted as being relative to the AppHost directory."
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
@JsonPropertyOrder({"type", "workingDirectory", "command", "args", "env", "bindings"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Executable extends Resource implements ResourceWithArguments<Executable>,
                                                    ResourceWithEnvironment<Executable>,
                                                    ResourceWithBindings<Executable> {

    @NotNull(message = "Executable.workingDirectory cannot be null")
    @NotEmpty(message = "Executable.workingDirectory cannot be an empty string")
    @JsonProperty("workingDirectory")
    private final String workingDirectory;

    @JsonProperty("command")
    @NotNull(message = "Executable.command cannot be null")
    @NotEmpty(message = "Executable.command cannot be an empty string")
    private final String command;

    @JsonProperty("args")
    @Valid
    private final List<String> arguments = new ArrayList<>();

    @JsonProperty("env")
    @Valid
    private final Map<String, String> env = new LinkedHashMap<>();

    @JsonProperty("bindings")
    @Valid
    private final List<Binding> bindings = new ArrayList<>();

    public Executable(String name, String workingDirectory, String command) {
        super("executable.v0", name);
        this.workingDirectory = workingDirectory;
        this.command = command;
    }

    public Executable withEnvironment(String key, String value) {
        this.env.put(key, value);
        return this;
    }

    @Override
    public Executable withArgument(String argument) {
        arguments.add(argument);
        return this;
    }

    @Override
    public Executable withBinding(Binding binding) {
        bindings.add(binding);
        return this;
    }
}
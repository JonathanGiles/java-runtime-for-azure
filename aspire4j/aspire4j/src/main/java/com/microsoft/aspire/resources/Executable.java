package com.microsoft.aspire.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.aspire.resources.traits.ResourceWithArguments;
import com.microsoft.aspire.resources.traits.ResourceWithEndpoints;
import com.microsoft.aspire.resources.traits.ResourceWithEnvironment;
import com.microsoft.aspire.resources.traits.ResourceWithReference;
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
public class Executable<T extends Executable<T>> extends Resource<T>
         implements ResourceWithArguments<T>, ResourceWithEnvironment<T>, ResourceWithEndpoints<T>, ResourceWithReference<T> {

    @NotNull(message = "Executable.workingDirectory cannot be null")
    @NotEmpty(message = "Executable.workingDirectory cannot be an empty string")
    @JsonProperty("workingDirectory")
    private String workingDirectory;

    @JsonProperty("command")
    @NotNull(message = "Executable.command cannot be null")
    @NotEmpty(message = "Executable.command cannot be an empty string")
    private String command;

    public Executable(String name) {
        this(name, null, null);
    }

    public Executable(String name, String workingDirectory) {
        this(name, workingDirectory, null);
    }

    public Executable(String name, String workingDirectory, String command) {
        super(ResourceType.EXECUTABLE, name);
        this.workingDirectory = workingDirectory;
        this.command = command;
    }

    public T withWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
        return self();
    }

    public T withCommand(String command) {
        this.command = command;
        return self();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T self() {
        return (T) this;
    }
}
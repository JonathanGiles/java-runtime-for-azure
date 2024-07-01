package com.microsoft.aspire.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.aspire.resources.properties.Binding;
import com.microsoft.aspire.resources.properties.EndpointReference;
import com.microsoft.aspire.resources.traits.ResourceWithArguments;
import com.microsoft.aspire.resources.traits.ResourceWithBindings;
import com.microsoft.aspire.resources.traits.ResourceWithEndpoints;
import com.microsoft.aspire.resources.traits.ResourceWithEnvironment;
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
                                                 implements ResourceWithArguments<Executable<T>>,
                                                          ResourceWithEnvironment<Executable<T>>,
                                                          ResourceWithBindings<Executable<T>>,
                                                          ResourceWithEndpoints<Executable<T>> {

    @NotNull(message = "Executable.workingDirectory cannot be null")
    @NotEmpty(message = "Executable.workingDirectory cannot be an empty string")
    @JsonProperty("workingDirectory")
    private String workingDirectory;

    @JsonProperty("command")
    @NotNull(message = "Executable.command cannot be null")
    @NotEmpty(message = "Executable.command cannot be an empty string")
    private String command;

    @JsonProperty("args")
    @Valid
    private final List<String> arguments = new ArrayList<>();

    @JsonProperty("env")
    @Valid
    private final Map<String, String> env = new LinkedHashMap<>();

    @JsonProperty("bindings")
    @Valid
    private final Map<Binding.Scheme, Binding> bindings = new LinkedHashMap<>();

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

    public T withEnvironment(String key, String value) {
        this.env.put(key, value);
        return self();
    }

    @Override
    @JsonIgnore
    public Map<String, String> getEnvironment() {
        return Collections.unmodifiableMap(env);
    }

    @Override
    @JsonIgnore
    public Executable withArgument(String argument) {
        arguments.add(argument);
        return this;
    }

    @Override
    @JsonIgnore
    public List<String> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    @JsonIgnore
    public Executable withBinding(Binding binding) {
        bindings.put(binding.getScheme(), binding);
        return this;
    }

    @Override
    @JsonIgnore
    public @Valid Map<Binding.Scheme, Binding> getBindings() {
        return Collections.unmodifiableMap(bindings);
    }

    @Override
    public List<EndpointReference> getEndpoints() {
        // TODO how do I know which endpoints are available?
        return List.of();
    }

    @Override
    public T self() {
        return (T) this;
    }
}
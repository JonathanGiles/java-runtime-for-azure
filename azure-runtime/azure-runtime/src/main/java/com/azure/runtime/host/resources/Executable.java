package com.azure.runtime.host.resources;

import com.azure.runtime.host.resources.traits.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.azure.runtime.host.resources.traits.ResourceWithArguments;
import com.azure.runtime.host.resources.traits.ResourceWithEndpoints;
import com.azure.runtime.host.resources.traits.ResourceWithEnvironment;
import com.azure.runtime.host.resources.traits.ResourceWithReference;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Represents an executable resource.
 *
 * @param <T> The specific type of the resource, which may or may not be a subtype of this class. This allows for
 *            method chaining, even when using a subtype, when used in conjunction with the API on
 *            {@link SelfAware}.
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

    /**
     * The path to the working directory. Should be interpreted as being relative to the AppHost directory.
     *
     * @param workingDirectory The path to the working directory.
     * @return The Executable resource.
     */
    public T withWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
        return self();
    }

    /**
     * The path to the command. Should be interpreted as being relative to the AppHost directory.
     *
     * @param command The path to the command.
     * @return The Executable resource.
     */
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
package com.microsoft.aspire.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.aspire.resources.properties.Binding;
import com.microsoft.aspire.resources.traits.ResourceWithBindings;
import com.microsoft.aspire.resources.traits.ResourceWithEnvironment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.*;

/*
 {
    "type": "object",
    "description": "A resource that represents a Dockerfile that will be built into a container duing deployment.",
    "required": [
        "type",
        "path",
        "context"
    ],
    "properties": {
        "type": {
            "const": "dockerfile.v0"
        },
        "path": {
            "type": "string",
            "description": "The file path to the Dockerfile to be built into a container image."
        },
        "context": {
            "type": "string",
            "description": "A directory path used as the context for building the Dockerfile into a container image."
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
@JsonPropertyOrder({"type", "path", "context", "env", "bindings"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DockerFile extends Resource implements ResourceWithEnvironment<DockerFile>,
        ResourceWithBindings<DockerFile> {

    @NotNull(message = "DockerFile.path cannot be null")
    @NotEmpty(message = "DockerFile.path cannot be an empty string")
    @JsonProperty("path")
    private final String path;

    @NotNull(message = "DockerFile.context cannot be null")
    @NotEmpty(message = "DockerFile.context cannot be an empty string")
    @JsonProperty("context")
    private final String context;

    @JsonProperty("env")
    @Valid
    private final Map<String, String> environment = new LinkedHashMap<>();

    @JsonProperty("bindings")
    @Valid
    private final Map<Binding.Scheme, Binding> bindings = new LinkedHashMap<>();

    public DockerFile(String name, String path, String context) {
        super("dockerfile.v0", name);
        this.path = Objects.requireNonNull(path);
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public DockerFile withEnvironment(String name, String value) {
        environment.put(name, value);
        return this;
    }

    @Override
    public DockerFile withBinding(Binding binding) {
        bindings.put(binding.getScheme(), binding);
        return this;
    }

    @Override
    public @Valid Map<Binding.Scheme, Binding> getBindings() {
        return Collections.unmodifiableMap(bindings);
    }
}

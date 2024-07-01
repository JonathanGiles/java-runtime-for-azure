package com.microsoft.aspire.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microsoft.aspire.implementation.json.RelativePath;
import com.microsoft.aspire.implementation.json.RelativePathSerializer;
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
    @JsonSerialize(using = RelativePathSerializer.class)
    @RelativePath
    private String path;

    @NotNull(message = "DockerFile.context cannot be null")
    @NotEmpty(message = "DockerFile.context cannot be an empty string")
    @JsonProperty("context")
    @JsonSerialize(using = RelativePathSerializer.class)
    @RelativePath
    private String context;

    @JsonProperty("env")
    @Valid
    private final Map<String, String> environment = new LinkedHashMap<>();

    @JsonProperty("bindings")
    @Valid
    private final Map<Binding.Scheme, Binding> bindings = new LinkedHashMap<>();

    public DockerFile(String name) {
        this(name, null, null);
    }

    public DockerFile(String name, String path) {
        this(name, path, null);
    }

    public DockerFile(String name, String path, String context) {
        super(ResourceType.DOCKER_FILE, name);
        this.path = path;
        this.context = context;
    }

    public DockerFile withPath(String path) {
        this.path = path;
        return this;
    }

    public DockerFile withContext(String context) {
        this.context = context;
        return this;
    }

    @Override
    @JsonIgnore
    public DockerFile withEnvironment(String name, String value) {
        environment.put(name, value);
        return this;
    }

    @Override
    @JsonIgnore
    public Map<String, String> getEnvironment() {
        return Collections.unmodifiableMap(environment);
    }

    @Override
    @JsonIgnore
    public DockerFile withBinding(Binding binding) {
        bindings.put(binding.getScheme(), binding);
        return this;
    }

    @Override
    @JsonIgnore
    public @Valid Map<Binding.Scheme, Binding> getBindings() {
        return Collections.unmodifiableMap(bindings);
    }
}

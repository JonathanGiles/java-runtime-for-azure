package com.azure.runtime.host.resources;

import com.azure.runtime.host.resources.traits.SelfAware;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.azure.runtime.host.resources.annotations.KeyValueAnnotation;
import com.azure.runtime.host.resources.traits.ResourceWithReference;
import com.azure.runtime.host.utils.json.RelativePath;
import com.azure.runtime.host.implementation.utils.json.RelativePathSerializer;
import com.azure.runtime.host.resources.traits.ResourceWithEndpoints;
import com.azure.runtime.host.resources.traits.ResourceWithEnvironment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

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

/**
 * A resource that represents a Dockerfile that will be built into a container during deployment.
 *
 * @param <T> The specific type of the resource, which may or may not be a subtype of this class. This allows for
 *           method chaining, even when using a subtype, when used in conjunction with the API on
 *           {@link SelfAware}.
 */
@JsonPropertyOrder({"type", "path", "context", "env", "bindings"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DockerFile<T extends DockerFile<T>> extends Resource<T>
        implements ResourceWithEnvironment<T>, ResourceWithEndpoints<T>, ResourceWithReference<T> { // FIXME Does a DockerFile support endpoints like this?

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

    public DockerFile(String name) {
        this(name, null, null);
    }

    public DockerFile(String name, String path) {
        this(name, path, null);
    }

    public DockerFile(String name, String path, String context) {
        this(ResourceType.DOCKER_FILE, name);
        this.path = path;
        this.context = context;
    }

    protected DockerFile(ResourceType type, String name) {
        super(type, name);
    }

    /**
     * Sets the path to the Dockerfile to be built into a container image. The path must be relative to the root
     * directory, i.e. from where azd is being executed from.
     * @param path A path to the Dockerfile, relative to the directory where azd will be executed from.
     * @return The DockerFile object.
     */
    public T withPath(String path) {
        this.path = path;
        return self();
    }

    /**
     * Sets the path to the context that will be used to build a container image. The path must be relative to the root
     * directory, i.e. from where azd is being executed from.
     *
     * @param context A path to the context, relative to the directory where azd will be executed from.
     * @return The DockerFile object.
     */
    public T withContext(String context) {
        this.context = context;
        return self();
    }
    
    public T withBuildArg(String key, String value) {
        withAnnotation(new KeyValueAnnotation("buildArgs", key, value));
        return self();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T self() {
        return (T) this;
    }
}

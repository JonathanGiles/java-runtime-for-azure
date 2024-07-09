package com.microsoft.aspire.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.aspire.resources.properties.*;
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
    "description": "A generic container resource.",
    "required": [
        "type",
        "image"
    ],
    "properties": {
        "type": {
            "const": "container.v0"
        },
        "image": {
            "type": "string",
            "description": "A string representing the container image to be used."
        },
        "entrypoint": {
            "type": "string",
            "description": "The entrypoint to use for the container image when executed."
        },
        "args": {
            "$ref": "#/definitions/args"
        },
        "connectionString": {
            "$ref": "#/definitions/connectionString"
        },
        "env": {
            "$ref": "#/definitions/env"
        },
        "bindings": {
            "$ref": "#/definitions/bindings"
        },
        "bindMounts": {
            "$ref": "#/definitions/bindMounts"
        },
        "volumes": {
            "$ref": "#/definitions/volumes"
        }
    },
    "additionalProperties": false
}

For example:

"basketcache": {
  "type": "container.v0",
  "connectionString": "{basketcache.bindings.tcp.host}:{basketcache.bindings.tcp.port}",
  "image": "docker.io/library/redis:7.2",
  "args": [
    "--save",
    "60",
    "1"
  ],
  "volumes": [
    {
      "name": "TestShop.AppHost-basketcache-data",
      "target": "/data",
      "readOnly": false
    }
  ],
  "bindings": {
    "tcp": {
      "scheme": "tcp",
      "protocol": "tcp",
      "transport": "tcp",
      "targetPort": 6379
    }
  }
}
 */
@JsonPropertyOrder({"type", "image", "entrypoint", "args", "connectionString", "env", "bindings", "bindMounts", "volumes"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Container<T extends Container<T>> extends Resource<T>
                                               implements ResourceWithArguments<T>,
                                                   ResourceWithEnvironment<T>,
                                                   ResourceWithEndpoints<T> {

    @NotNull(message = "Container.image cannot be null")
    @NotEmpty(message = "Container.image cannot be an empty string")
    @JsonProperty("image")
    private String image;

    @JsonProperty("entrypoint")
    private String entryPoint;

    @JsonProperty("args")
    @Valid
    private final List<String> arguments = new ArrayList<>();

    @JsonProperty("volumes")
    @Valid
    private final List<Volume> volumes = new ArrayList<>();

    @JsonProperty("bindMounts")
    @Valid
    private final List<BindMount> bindMounts = new ArrayList<>();

    public Container(String name) {
        this(name, null);
    }

    public Container(String name, String image) {
        super(ResourceType.CONTAINER, name);
        this.image = image;
    }

    @JsonIgnore
    public T withImage(String image) {
        this.image = image;
        return self();
    }

    @JsonIgnore
    public T withEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
        return self();
    }

    @Override
    @JsonIgnore
    public T withArgument(String argument) {
        arguments.add(argument);
        return self();
    }

    @Override
    @JsonIgnore
    public List<String> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @JsonIgnore
    public T withVolume(Volume volume) {
        volumes.add(volume);
        return self();
    }

    @JsonIgnore
    public T withBindMount(BindMount bindMount) {
        bindMounts.add(bindMount);
        return self();
    }

    @Override
    public T self() {
        return (T) this;
    }
}

package com.microsoft.aspire.components.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.aspire.components.common.traits.ResourceWithArguments;
import com.microsoft.aspire.components.common.traits.ResourceWithBindings;
import com.microsoft.aspire.components.common.traits.ResourceWithEnvironment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public class Container extends Resource implements ResourceWithArguments<Container>,
                                                   ResourceWithEnvironment<Container>,
                                                   ResourceWithBindings<Container> {

    @JsonProperty("image")
    private final String image;

    @JsonProperty("entrypoint")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String entryPoint;

    @JsonProperty("args")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<String> arguments = new ArrayList<>();

    @JsonProperty("volumes")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<Volume> volumes = new ArrayList<>();

    @JsonProperty("env")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final Map<String, String> environment = new LinkedHashMap<>();

    public Container(String name, String image) {
        super("container.v0", name);
        this.image = image;
    }

    public Container withEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
        return this;
    }

    @Override
    public Container withArgument(String argument) {
        arguments.add(argument);
        return this;
    }

    public Container withVolume(Volume volume) {
        volumes.add(volume);
        return this;
    }

    public Container withDataVolume() {
        // placeholder values from https://github.com/dotnet/aspire/blob/main/playground/TestShop/AppHost/aspire-manifest.json#L38
        volumes.add(new Volume("TestShop.AppHost-basketcache-data", "/data", false));
        return this;
    }

    public Container withEnvironment(String name, String value) {
        environment.put(name, value);
        return this;
    }

    public Container withBinding(Binding binding) {
        // TODO
        return this;
    }
}

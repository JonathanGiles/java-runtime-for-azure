package com.microsoft.aspire.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.aspire.resources.properties.BindMount;
import com.microsoft.aspire.resources.properties.Binding;
import com.microsoft.aspire.resources.properties.Volume;
import com.microsoft.aspire.resources.traits.ResourceWithArguments;
import com.microsoft.aspire.resources.traits.ResourceWithBindings;
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
public class Container extends Resource implements ResourceWithArguments<Container>,
        ResourceWithEnvironment<Container>,
        ResourceWithBindings<Container> {

    @NotNull(message = "Container.image cannot be null")
    @NotEmpty(message = "Container.image cannot be an empty string")
    @JsonProperty("image")
    private final String image;

    @JsonProperty("entrypoint")
    private String entryPoint;

    @JsonProperty("args")
    @Valid
    private final List<String> arguments = new ArrayList<>();

    @JsonProperty("volumes")
    @Valid
    private final List<Volume> volumes = new ArrayList<>();

    @JsonProperty("env")
    private final Map<String, String> environment = new LinkedHashMap<>();

    @JsonProperty("bindings")
    @Valid
    private final Map<Binding.Scheme, Binding> bindings = new LinkedHashMap<>();

    @JsonProperty("bindMounts")
    @Valid
    private final List<BindMount> bindMounts = new ArrayList<>();


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
        // FIXME: hardcoded values
        // placeholder values from https://github.com/dotnet/aspire/blob/main/playground/TestShop/AppHost/aspire-manifest.json#L38
        volumes.add(new Volume("TestShop.AppHost-basketcache-data", "/data", false));
        return this;
    }

    @Override
    public Container withEnvironment(String name, String value) {
        environment.put(name, value);
        return this;
    }

    @Override
    public Container withBinding(Binding binding) {
        bindings.put(binding.getScheme(), binding);
        return this;
    }

    @Override
    public @Valid Map<Binding.Scheme, Binding> getBindings() {
        return Collections.unmodifiableMap(bindings);
    }

    // TODO should this be part of ResourceWithBindings?
    public Container withBindMount(BindMount bindMount) {
        bindMounts.add(bindMount);
        return this;
    }
}

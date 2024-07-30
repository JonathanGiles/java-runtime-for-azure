package com.azure.runtime.host.resources;

import com.azure.runtime.host.resources.traits.SelfAware;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.azure.runtime.host.resources.traits.ResourceWithParameters;
import com.azure.runtime.host.resources.traits.ResourceWithTemplate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/*
{
    "type": "object",
    "default": "Represents a resource that is deployed using Azure Bicep.",
    "required": [
        "path"
    ],
    "properties": {
        "type": {
            "const": "azure.bicep.v0"
        },
        "path": {
            "type": "string",
            "description": "Path to the Bicep file to be used for deployment."
        },
        "connectionString": {
            "$ref": "#/definitions/connectionString"
        },
        "params": {
            "type": "object",
            "description": "A list of parameters which are passed to Azure deployment.",
            "additionalProperties": {
                "oneOf": [
                    {
                        "type": "array"
                    },
                    {
                        "type": "boolean"
                    },
                    {
                        "type": "number"
                    },
                    {
                        "type": "object"
                    },
                    {
                        "type": "string"
                    }
                ]
            }
        }
    },
    "additionalProperties": false
},

For example:

"storage": {
  "type": "azure.bicep.v0",
  "path": "storage.module.bicep",
  "params": {
    "principalId": "",
    "principalType": ""
  }
},
 */

/**
 * Represents a {@link Resource} that is deployed using Azure Bicep.
 * @param <T> The specific type of the resource, which may or may not be a subtype of this class. This allows for
 *           method chaining, even when using a subtype, when used in conjunction with the API on
 *           {@link SelfAware}.
 */
@JsonPropertyOrder({"type", "path", "connectionString", "params"})
public abstract class AzureBicepResource<T extends AzureBicepResource<T>>
                                        extends Resource<T>
                                        implements ResourceWithParameters<T>,
                                                   ResourceWithTemplate<T> {

    @NotNull(message = "AzureBicep.path cannot be null")
    @NotEmpty(message = "AzureBicep.path cannot be an empty string")
    @JsonProperty("path")
    private String path;

    @Valid
    @JsonProperty("params")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> parameters = new LinkedHashMap<>();

    public AzureBicepResource(String name) {
        this(name, null);
    }

    public AzureBicepResource(ResourceType type, String name) {
        this(type, name, null);
    }

    public AzureBicepResource(String name, String path) {
        this(ResourceType.AZURE_BICEP, name, path);
    }

    public AzureBicepResource(ResourceType type, String name, String path) {
        super(type, name);
        this.path = path;
    }

    @JsonIgnore
    public T withPath(String path) {
        this.path = path;
        return self();
    }

    @Override
    @JsonIgnore
    public T withParameter(String name, Object value) {
        parameters.put(name, value);
        return self();
    }

    @Override
    @JsonIgnore
    public @Valid Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T self() {
        return (T) this;
    }
}

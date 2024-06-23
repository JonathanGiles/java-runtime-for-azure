package com.microsoft.aspire.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.aspire.resources.traits.ResourceWithParameters;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

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
@JsonPropertyOrder({"type", "path", "connectionString", "params"})
public class AzureBicep extends Resource implements ResourceWithParameters<AzureBicep> {

    @NotNull(message = "AzureBicep.path cannot be null")
    @NotEmpty(message = "AzureBicep.path cannot be an empty string")
    @JsonProperty("path")
    private String path;

    @Valid
    @JsonProperty("params")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> parameters = new LinkedHashMap<>();

    public AzureBicep(String name) {
        this(name, null);
    }

    public AzureBicep(String name, String path) {
        super(ResourceType.AZURE_BICEP, name);
        this.path = path;
    }

    public AzureBicep withPath(String path) {
        this.path = path;
        return this;
    }

    public AzureBicep withParameter(String name, String value) {
        parameters.put(name, value);
        return this;
    }
}

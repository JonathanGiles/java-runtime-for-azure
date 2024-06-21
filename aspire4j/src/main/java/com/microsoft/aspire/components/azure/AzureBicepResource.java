package com.microsoft.aspire.components.azure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.aspire.components.common.Resource;
import com.microsoft.aspire.components.common.traits.ResourceWithParameters;
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
public class AzureBicepResource extends Resource implements ResourceWithParameters<AzureBicepResource> {

    @NotNull(message = "AzureBicep.path cannot be null")
    @NotEmpty(message = "AzureBicep.path cannot be an empty string")
    @JsonProperty("path")
    private final String path;

    @Valid
    @JsonProperty("params")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> parameters = new LinkedHashMap<>();

    public AzureBicepResource(String name, String path) {
        super("azure.bicep.v0", name);
        this.path = path;
    }

    public AzureBicepResource withParameter(String name, String value) {
        parameters.put(name, value);
        return this;
    }
}

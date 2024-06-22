package com.microsoft.aspire.resources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/*
{
    "type": "object",
    "required": [
        "name",
        "target",
        "readOnly"
    ],
    "properties": {
        "name": {
            "type": "string",
            "description": "The name of the volume."
        },
        "target": {
            "type": "string",
            "description": "The target within the container where the volume is mounted."
        },
        "readOnly": {
            "type": "boolean",
            "description": "Flag indicating whether the mount is read-only."
        }
    },
    "additionalProperties": false
}
 */
public class Volume {
    @NotNull(message = "Volume.name cannot be null")
    @NotEmpty(message = "Volume.name cannot be an empty string")
    @JsonProperty("name")
    private final String name;

    @NotNull(message = "Volume.target cannot be null")
    @NotEmpty(message = "Volume.target cannot be an empty string")
    @JsonProperty("target")
    private final String target;

    @JsonProperty("readOnly")
    private final boolean readOnly;

    public Volume(String name, String target, boolean readOnly) {
        this.name = name;
        this.target = target;
        this.readOnly = readOnly;
    }
}

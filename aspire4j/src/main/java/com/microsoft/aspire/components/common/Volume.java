package com.microsoft.aspire.components.common;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonProperty
    private final String name;

    @JsonProperty
    private final String target;

    @JsonProperty
    private final boolean readOnly;

    public Volume(String name, String target, boolean readOnly) {
        this.name = name;
        this.target = target;
        this.readOnly = readOnly;
    }
}

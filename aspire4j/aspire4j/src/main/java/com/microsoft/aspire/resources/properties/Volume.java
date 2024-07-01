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
    private String name;

    @NotNull(message = "Volume.target cannot be null")
    @NotEmpty(message = "Volume.target cannot be an empty string")
    @JsonProperty("target")
    private String target;

    @JsonProperty("readOnly")
    private boolean readOnly;

    public Volume() {
    }

    public Volume(String name) {
        this(name, null);
    }

    public Volume(String name, String target) {
        this(name, target, false);
    }

    public Volume(String name, String target, boolean readOnly) {
        this.name = name;
        this.target = target;
        this.readOnly = readOnly;
    }

    public Volume withName(String name) {
        this.name = name;
        return this;
    }

    public Volume withTarget(String target) {
        this.target = target;
        return this;
    }

    public Volume withReadOnly() {
        this.readOnly = true;
        return this;
    }
}

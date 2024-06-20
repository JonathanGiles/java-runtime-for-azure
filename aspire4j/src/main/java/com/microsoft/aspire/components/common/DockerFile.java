package com.microsoft.aspire.components.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

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
public class DockerFile extends Resource {

    @JsonProperty("path")
    private final String path;

    public DockerFile(String type, String name, String path) {
        super(type, name);
        this.path = Objects.requireNonNull(path);
    }
}

package com.microsoft.aspire.resources;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.LinkedHashMap;
import java.util.Map;

/*
{
    "type": "object",
    "required": [
        "connectionString"
    ],
    "description": "Represents a value resource. Typically used to perform string concatenation (e.g. for connection strings).",
    "properties": {
        "type": {
            "const": "value.v0"
        },
        "connectionString": {
            "$ref": "#/definitions/connectionString"
        }
    },
    "additionalProperties": false
},
 */
@JsonPropertyOrder({"connectionString"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Value extends Resource {

    @NotEmpty(message = "Value.properties cannot be empty")
    @JsonIgnore
    private final Map<String, String> properties = new LinkedHashMap<>();

    public Value(String name, String key, String value) {
        super(ResourceType.VALUE, name);
        properties.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, String> getProperties() {
        return properties;
    }
}

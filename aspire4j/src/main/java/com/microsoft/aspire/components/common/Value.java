package com.microsoft.aspire.components.common;

import com.fasterxml.jackson.annotation.*;
import com.microsoft.aspire.components.common.properties.BindMount;
import com.microsoft.aspire.components.common.properties.Binding;
import com.microsoft.aspire.components.common.properties.Volume;
import com.microsoft.aspire.components.common.traits.ResourceWithArguments;
import com.microsoft.aspire.components.common.traits.ResourceWithBindings;
import com.microsoft.aspire.components.common.traits.ResourceWithEnvironment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
        super("value.v0", name);
        properties.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, String> getProperties() {
        return properties;
    }
}

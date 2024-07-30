package com.azure.runtime.host.resources.annotations;

import java.util.Objects;

public class KeyValueAnnotation implements ResourceAnnotation {
    private final String type;
    private final String key;
    private final Object value;

    public KeyValueAnnotation(String type, String key, Object value) {
        this.type = Objects.requireNonNull(type, "Type cannot be null");
        this.key = Objects.requireNonNull(key, "Key cannot be null");
        this.value = Objects.requireNonNull(value, "Value cannot be null");
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "KeyValueAnnotation{" +
            "type='" + type + '\'' +
            ", key='" + key + '\'' +
            ", value='" + value + '\'' +
            '}';
    }
}
package com.azure.runtime.host.resources.annotations;

import java.util.Objects;

public class EnvironmentAnnotation extends EnvironmentCallbackAnnotation {
    private final String name;
    private final String value;

    public EnvironmentAnnotation(String name, String value) {
        super(name, () -> value);
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.value = Objects.requireNonNull(value, "Value cannot be null");
    }

    @Override
    public String toString() {
        return "EnvironmentAnnotation{" +
            "name='" + name + '\'' +
            ", value='" + value + '\'' +
            '}';
    }
}
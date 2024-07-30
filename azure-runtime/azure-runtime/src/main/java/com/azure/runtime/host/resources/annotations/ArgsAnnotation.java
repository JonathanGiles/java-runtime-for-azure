package com.azure.runtime.host.resources.annotations;

import java.util.List;
import java.util.Objects;

public class ArgsAnnotation implements ResourceAnnotation {
    private final String type;
    private final List<Object> args;

    public static ArgsAnnotation createArgs(Object value) {
        return createArgs(List.of(value));
    }

    public static ArgsAnnotation createArgs(List<Object> values) {
        return new ArgsAnnotation("args", values);
    }

    private ArgsAnnotation(String type, List<Object> values) {
        this.type = Objects.requireNonNull(type, "Type cannot be null");
        Objects.requireNonNull(values, "Value cannot be null");
        this.args = values;
    }

    public String getType() {
        return type;
    }

    public List<Object> getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "ArgsAnnotation{" +
            "type='" + type + '\'' +
            ", args='" + args.toString() + '\'' +
            '}';
    }
}

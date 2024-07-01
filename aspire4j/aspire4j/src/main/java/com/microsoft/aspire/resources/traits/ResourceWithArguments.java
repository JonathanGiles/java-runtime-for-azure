package com.microsoft.aspire.resources.traits;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public interface ResourceWithArguments<T extends ResourceWithArguments<T>> {

    T withArgument(String argument);

    /**
     * Returns an unmodifiable list of arguments.
     */
    @JsonIgnore
    List<String> getArguments();

    default T withArguments(String... arguments) {
        for (String argument : arguments) {
            withArgument(argument);
        }

        return (T) this;
    }

    default T withArguments(Iterable<String> arguments) {
        for (String argument : arguments) {
            withArgument(argument);
        }
        return (T) this;
    }
}

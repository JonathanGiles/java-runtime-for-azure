package com.microsoft.aspire.resources.traits;

public interface ResourceWithArguments<T extends ResourceWithArguments<T>> {

    T withArgument(String argument);

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

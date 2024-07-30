package com.azure.runtime.host.resources.annotations;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents an annotation that provides a callback to modify the environment variables of an application.
 */
public class EnvironmentCallbackAnnotation implements ResourceAnnotation {
    private final String name;
    private final Consumer<EnvironmentCallbackContext> callback;

    private String debugHint;

    /**
     * Initializes a new instance of the {@code EnvironmentCallbackAnnotation} class with the specified name and callback function.
     *
     * @param name     The name of the environment variable to set.
     * @param supplier The callback function that returns the value to set the environment variable to.
     */
    public EnvironmentCallbackAnnotation(final String name, final Supplier<String> supplier) {
        if (name == null || supplier == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        this.name = name;
        this.callback = context -> {
            context.getEnvironmentVariables().put(name, supplier.get());
        };
    }

//    /**
//     * Initializes a new instance of the {@code EnvironmentCallbackAnnotation} class with the specified callback action.
//     *
//     * @param callback The callback action to be executed.
//     */
//    public EnvironmentCallbackAnnotation(Consumer<Map<String, Object>> callback) {
//        if (callback == null) {
//            throw new IllegalArgumentException("Callback cannot be null");
//        }
//        this.name = null;
//        this.callback = context -> {
//            callback.accept(context.getEnvironmentVariables());
//            return CompletableFuture.completedFuture(null);
//        };
//    }
//
    /**
     * Initializes a new instance of the {@code EnvironmentCallbackAnnotation} class with the specified callback.
     *
     * @param callback The callback to be invoked.
     */
    public EnvironmentCallbackAnnotation(String debugHint, Consumer<EnvironmentCallbackContext> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback cannot be null");
        }
        this.debugHint = debugHint;
        this.name = null;
        this.callback = callback;
    }
//
//    /**
//     * Initializes a new instance of the {@code EnvironmentCallbackAnnotation} class with the specified callback.
//     *
//     * @param callback The callback to be invoked.
//     */
//    public EnvironmentCallbackAnnotation(Function<EnvironmentCallbackContext, CompletionStage<Void>> callback) {
//        if (callback == null) {
//            throw new IllegalArgumentException("Callback cannot be null");
//        }
//        this.name = null;
//        this.callback = callback;
//    }
//
    /**
     * Gets the callback action to be executed when the environment is being built.
     *
     * @return The callback action.
     */
    public Consumer<EnvironmentCallbackContext> getCallback() {
        return callback;
    }

    @Override
    public String toString() {
        return "EnvironmentCallbackAnnotation{" +
            "name='" + name + '\'' +
            ", debugHint='" + debugHint + '\'' +
            '}';
    }
}
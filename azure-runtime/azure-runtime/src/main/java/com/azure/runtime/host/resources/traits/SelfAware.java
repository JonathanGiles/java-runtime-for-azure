package com.azure.runtime.host.resources.traits;

/**
 * A class is self aware if it can return a reference to itself. This allows for fluent API design, where we can return
 * subtypes from a method, depending on which type of object we are working with.
 * @param <T> The type of the class that is self aware.
 */
public interface SelfAware<T> {

    /**
     * Returns a reference to the object that implements this interface.
     * @return A reference to the object that implements this interface.
     */
    T self();
}

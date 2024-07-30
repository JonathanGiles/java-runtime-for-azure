package com.azure.runtime.host.resources.traits;

/**
 * Interface for resources that can introspect themselves. This is useful for resources that need to look at the
 * environment they are running in to determine their configuration, for example to look at referenced projects, or
 * to determine properties from remote resources.
 * <p>
 * Note that the App Host never directly calls the introspect() method. Instead, calling the introspect() method
 * is the responsibility of the resource creator. This is because the resource creator is the one who knows when the
 * resource is ready to be introspected. Introspective resources should implement the appropriate lifecycle methods
 * in {@link ResourceWithLifecycle} to ensure that they are introspected at the right time.
 *
 * @see ResourceWithLifecycle
 */
public interface IntrospectiveResource {
    void introspect();
}

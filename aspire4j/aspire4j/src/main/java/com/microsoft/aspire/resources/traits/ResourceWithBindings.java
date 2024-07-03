package com.microsoft.aspire.resources.traits;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microsoft.aspire.resources.properties.Binding;

import java.util.Map;

public interface ResourceWithBindings<T extends ResourceWithBindings<T>> extends SelfAware<T> {

    /**
     * Adds a binding to the resource.
     * @param binding The binding to add.
     * @return The resource with the added binding.
     */
    T withBinding(Binding binding);

    /**
     * Returns a read-only map of bindings for this resource.
     * @return A read-only map of bindings for this resource.
     */
    @JsonIgnore
    Map<Binding.Scheme, Binding> getBindings();

    /**
     * Marks existing http or https endpoints on a resource as external.
     * @return The resource with updated endpoints.
     */
    default T withExternalHttpEndpoints() {
        getBindings().values().stream()
            .filter(binding -> binding.getScheme() == Binding.Scheme.HTTP || binding.getScheme() == Binding.Scheme.HTTPS)
            .forEach(Binding::withExternal);

        return self();
    }
}

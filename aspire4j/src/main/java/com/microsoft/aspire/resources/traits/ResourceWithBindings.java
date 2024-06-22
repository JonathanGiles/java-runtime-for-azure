package com.microsoft.aspire.resources.traits;

import com.microsoft.aspire.resources.properties.Binding;

import java.util.Map;

public interface ResourceWithBindings<T extends ResourceWithBindings<T>> {

    T withBinding(Binding binding);

    /**
     * Returns a read-only map of bindings for this resource.
     * @return
     */
    Map<Binding.Scheme, Binding> getBindings();

    default T withExternalHttpEndpoints() {
        withBinding(new Binding(Binding.Scheme.HTTP, Binding.Protocol.TCP, Binding.Transport.HTTP).withTargetPort(8080).withExternal());
        withBinding(new Binding(Binding.Scheme.HTTPS, Binding.Protocol.TCP, Binding.Transport.HTTP).withTargetPort(8080).withExternal());
        return (T) this;
    }
}

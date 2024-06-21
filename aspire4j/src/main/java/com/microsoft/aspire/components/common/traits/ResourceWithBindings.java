package com.microsoft.aspire.components.common.traits;

import com.microsoft.aspire.components.common.Project;
import com.microsoft.aspire.components.common.properties.Binding;

public interface ResourceWithBindings<T extends ResourceWithBindings<T>> {

    T withBinding(Binding binding);

    default T withExternalHttpEndpoints() {
        withBinding(new Binding(Binding.Scheme.HTTP, Binding.Protocol.TCP, Binding.Transport.HTTP).withTargetPort(8080).withExternal());
        withBinding(new Binding(Binding.Scheme.HTTPS, Binding.Protocol.TCP, Binding.Transport.HTTP).withTargetPort(8080).withExternal());
        return (T) this;
    }
}

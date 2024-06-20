package com.microsoft.aspire.components.common.traits;

import com.microsoft.aspire.components.common.Binding;

public interface ResourceWithBindings<T extends ResourceWithBindings<T>> {

    T withBinding(Binding binding);
}

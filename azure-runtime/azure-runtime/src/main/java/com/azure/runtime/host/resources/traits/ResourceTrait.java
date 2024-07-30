package com.azure.runtime.host.resources.traits;

import com.azure.runtime.host.resources.Resource;

public interface ResourceTrait<T extends Resource<T>> extends SelfAware<T> {
}

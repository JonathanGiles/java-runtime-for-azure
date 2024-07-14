package com.microsoft.aspire.resources.traits;

import com.microsoft.aspire.resources.Resource;

public interface ResourceTrait<T extends Resource<T>> extends SelfAware<T> {
}

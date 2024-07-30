package com.azure.runtime.host.resources.traits;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.azure.runtime.host.resources.Resource;

/**
 * Represents a resource that has a parent resource.
 * @param <T>
 */
public interface ResourceWithParent<T extends Resource<T>> {

    /**
     * Gets the parent resource.
     * @return
     */
    @JsonIgnore
    T getParent();

}

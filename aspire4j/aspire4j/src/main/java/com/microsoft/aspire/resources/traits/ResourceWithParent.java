package com.microsoft.aspire.resources.traits;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microsoft.aspire.resources.Resource;

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

package com.microsoft.aspire.resources.traits;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a resource that has a parent resource.
 * @param <T>
 */
public interface ResourceWithParent<T> {

    /**
     * Gets the parent resource.
     * @return
     */
    @JsonIgnore
    T getParent();

}

package com.azure.runtime.host.resources.traits;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * An interface that allows the value to list its references.
 */
public interface ValueWithReferences {
    /**
     * A read-only list of the referenced objects of the value.
     * @return A read-only list of the referenced objects.
     */
    @JsonIgnore
    List<Object> getReferences();
}

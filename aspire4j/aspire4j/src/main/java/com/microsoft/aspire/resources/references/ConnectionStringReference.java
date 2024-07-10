package com.microsoft.aspire.resources.references;

import com.microsoft.aspire.resources.Resource;
import com.microsoft.aspire.resources.traits.ManifestExpressionProvider;
import com.microsoft.aspire.resources.traits.ResourceWithConnectionString;
import com.microsoft.aspire.resources.traits.ValueProvider;
import com.microsoft.aspire.resources.traits.ValueWithReferences;

import java.util.Collections;
import java.util.List;

/**
 * Represents a reference to a connection string.
 */
public class ConnectionStringReference<T extends Resource<?> & ResourceWithConnectionString<?>>
                                            implements ManifestExpressionProvider, ValueProvider, ValueWithReferences {

    private final T resource;
    private final boolean optional;

    /**
     * Initializes a new instance of the ConnectionStringReference class.
     *
     * @param resource The resource that the connection string is referencing.
     * @param optional A flag indicating whether the connection string is optional.
     */
    public ConnectionStringReference(T resource, boolean optional) {
        if (resource == null) {
            throw new NullPointerException("resource cannot be null");
        }
        this.resource = resource;
        this.optional = optional;
    }

    /**
     * Gets the resource that the connection string is referencing.
     *
     * @return The resource.
     */
    public T getResource() {
        return resource;
    }

    /**
     * Gets a flag indicating whether the connection string is optional.
     *
     * @return True if optional, false otherwise.
     */
    public boolean isOptional() {
        return optional;
    }

    @Override
    public String getValueExpression() {
        return resource.getValueExpression();
    }

    @Override
    public List<Object> getReferences() {
        return Collections.singletonList(resource);
    }

    @Override
    public String getValue() {
        String value = resource.getValue();
        if (value == null || value.isEmpty() && !optional) {
            throw new RuntimeException("The connection string for the resource '" + resource.getName() + "' is not available.");
        }
        return value;
    }
}
package com.azure.runtime.host.resources.traits;

/**
 * An interface that allows an object to express how it should be represented in a manifest.
 */
public interface ManifestExpressionProvider {

    /**
     * Gets the expression that represents a value in manifest.
     * @return The expression that represents a value in manifest.
     */
    String getValueExpression();
}

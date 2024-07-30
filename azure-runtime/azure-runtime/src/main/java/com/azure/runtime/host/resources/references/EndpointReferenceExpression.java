package com.azure.runtime.host.resources.references;

import com.azure.runtime.host.resources.traits.ManifestExpressionProvider;
import com.azure.runtime.host.resources.traits.ValueProvider;
import com.azure.runtime.host.resources.traits.ValueWithReferences;

import java.util.List;

/**
 * Represents a property expression for an endpoint reference.
 */
public class EndpointReferenceExpression implements ManifestExpressionProvider, ValueProvider, ValueWithReferences {
    private final EndpointReference<?> endpointReference;
    private final EndpointProperty property;

    /**
     * Initializes a new instance of the {@code EndpointReferenceExpression} class.
     *
     * @param endpointReference The endpoint reference.
     * @param property          The property of the endpoint.
     */
    public EndpointReferenceExpression(EndpointReference<?> endpointReference, EndpointProperty property) {
        if (endpointReference == null) {
            throw new IllegalArgumentException("endpointReference cannot be null");
        }
        this.endpointReference = endpointReference;
        this.property = property;
    }

    /**
     * Gets the {@code EndpointReference}.
     *
     * @return The endpoint reference.
     */
    public EndpointReference<?> getEndpointReference() {
        return endpointReference;
    }

    /**
     * Gets the {@code EndpointProperty} for the property expression.
     *
     * @return The endpoint property.
     */
    public EndpointProperty getProperty() {
        return property;
    }

    /**
     * Gets the expression of the property of the endpoint.
     *
     * @return The value expression.
     */
    public String getValueExpression() {
        return endpointReference.getValueExpression();
    }

    /**
     * Gets the value of the property of the endpoint.
     *
     * @return A {@code CompletableFuture} containing the selected {@code EndpointProperty} value.
     * @throws IllegalStateException Throws when the selected {@code EndpointProperty} enumeration is not known.
     */
    public String getValue() {
        return switch (property) {
            case URL -> endpointReference.getUrl();
            case HOST -> endpointReference.getHost();
            case IPV4_HOST -> "127.0.0.1";
            case PORT -> String.valueOf(endpointReference.getPort());
            case SCHEME -> endpointReference.getScheme().toString();
            case TARGET_PORT -> computeTargetPort();
            default ->
                throw new IllegalStateException("The property '" + property + "' is not supported for the endpoint '" + endpointReference.getEndpointName() + "'.");
        };
    }

    private String computeTargetPort() {
        // TODO
        // Placeholder implementation for computing the target port.
        // The actual implementation should resolve the target port at runtime.
        return endpointReference.getTargetPort() != null ? String.valueOf(endpointReference.getTargetPort()) : "Dynamic";
    }

    @Override
    public List<Object> getReferences() {
        return List.of(endpointReference);
    }
}
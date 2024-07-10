package com.microsoft.aspire.resources.references;

import com.microsoft.aspire.implementation.TemplateStrings;
import com.microsoft.aspire.resources.Resource;
import com.microsoft.aspire.resources.annotations.EndpointAnnotation;
import com.microsoft.aspire.resources.properties.AllocatedEndpoint;
import com.microsoft.aspire.resources.properties.EndpointProperty;
import com.microsoft.aspire.resources.properties.Scheme;
import com.microsoft.aspire.resources.traits.ManifestExpressionProvider;
import com.microsoft.aspire.resources.traits.ResourceWithEndpoints;
import com.microsoft.aspire.resources.traits.ValueProvider;
import com.microsoft.aspire.resources.traits.ValueWithReferences;

import java.util.List;
import java.util.Optional;

/**
 * An EndpointReference is a core Aspire type used for keeping track of endpoint details in expressions. Simple literal
 * values cannot be used because endpoints are not known until containers are launched.
 */
public class EndpointReference<T extends Resource<?> & ResourceWithEndpoints<?>>
                implements ValueProvider, ValueWithReferences, ManifestExpressionProvider {

    private final String endpointName;
    private final T resource;
    private final EndpointAnnotation endpointAnnotation;

    /**
     * Creates a new instance of EndpointReference with the specified endpoint name.
     * @param owner The resource with endpoints that owns the endpoint reference.
     * @param endpointAnnotation The endpoint annotation.
     */
    public EndpointReference(T owner, EndpointAnnotation endpointAnnotation) {
        this.resource = owner;
        this.endpointName = endpointAnnotation.getName();
        this.endpointAnnotation = endpointAnnotation;
    }

    /**
     * Creates a new instance of EndpointReference with the specified endpoint name.
     * @param owner The resource with endpoints that owns the endpoint reference.
     * @param endpointName The name of the endpoint.
     */
    public EndpointReference(T owner, String endpointName) {
        this.resource = owner;
        this.endpointName = endpointName;
        this.endpointAnnotation = null;
    }

    @Override
    public String getValue() {
        return getAllocatedEndpoint()
            .map(AllocatedEndpoint::getUriString)
            .orElse(null);
    }

    @Override
    public String getValueExpression() {
        return getExpression(EndpointProperty.URL);
    }

    // FIXME this is not the long term plan!
    private String getExpression(EndpointProperty property) {
        String prop = switch (property) {
            case URL -> "url";
            case HOST, IPV4_HOST -> "host";
            case PORT -> "port";
            case SCHEME -> "scheme";
            case TARGET_PORT -> "targetPort";
        };

        return TemplateStrings.evaluateBinding(resource, endpointName, prop);
    }

    @Override
    public List<Object> getReferences() {
        return List.of(resource);
    }

    private Optional<EndpointAnnotation> getEndpointAnnotation() {
        return Optional.ofNullable(endpointAnnotation);
    }

    private Optional<AllocatedEndpoint> getAllocatedEndpoint() {
        return getEndpointAnnotation().map(EndpointAnnotation::getAllocatedEndpoint);
    }

    /**
     * Gets the container host for this endpoint.
     *
     * @return The container host.
     */
    public String getContainerHost() {
        return getAllocatedEndpoint()
            .map(AllocatedEndpoint::getContainerHostAddress)
            .orElseThrow(() -> new IllegalStateException("The endpoint \"" + endpointName + "\" has no associated container host name."));
    }

    /**
     * Gets the name of the endpoint associated with this reference.
     *
     * @return The endpoint name.
     */
    public String getEndpointName() {
        return endpointName;
    }

    /**
     * Gets a value indicating whether the endpoint exists.
     *
     * @return true if the endpoint exists, false otherwise.
     */
    public boolean isExists() {
        return getEndpointAnnotation().isPresent();
    }

    /**
     * Gets the host for this endpoint.
     *
     * @return The host.
     */
    public String getHost() {
        return getEndpointAnnotation()
            .map(EndpointAnnotation::getAllocatedEndpoint)
            .map(AllocatedEndpoint::getAddress)
            .orElse("localhost");
    }

    /**
     * Checks if the endpoint is allocated.
     *
     * @return true if the endpoint is allocated, false otherwise.
     */
    public boolean isAllocated() {
        return getAllocatedEndpoint().isPresent();
    }

    /**
     * Gets the port for this endpoint.
     *
     * @return The port.
     */
    public int getPort() {
        return getAllocatedEndpoint()
            .map(AllocatedEndpoint::getPort)
            .orElseThrow(() -> new IllegalStateException("The endpoint \"" + endpointName + "\" has no associated port."));
    }

    /**
     * Gets the resource owner of the endpoint reference.
     *
     * @return The resource owner.
     */
    public T getResource() {
        return resource;
    }

    /**
     * Gets the scheme for this endpoint.
     *
     * @return The scheme.
     */
    public Scheme getScheme() {
        return getEndpointAnnotation()
            .map(EndpointAnnotation::getUriScheme)
            .orElse(null);
    }

    /**
     * Gets the target port for this endpoint. Returns null if the port is dynamically allocated.
     *
     * @return The target port, or null if dynamically allocated.
     */
    public Integer getTargetPort() {
        return getEndpointAnnotation()
            .map(EndpointAnnotation::getTargetPort)
            .orElse(null);
    }

    /**
     * Gets the URL for this endpoint.
     *
     * @return The URL.
     */
    public String getUrl() {
        return getAllocatedEndpoint()
            .map(AllocatedEndpoint::getUriString)
            .orElse(null);
    }
}
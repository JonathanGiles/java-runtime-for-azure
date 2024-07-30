package com.azure.runtime.host.resources.properties;

import com.azure.runtime.host.resources.annotations.EndpointAnnotation;

import java.util.Objects;

/**
 * Represents an endpoint allocated for a service instance.
 */
public class AllocatedEndpoint {
    private final EndpointAnnotation endpoint;
    private final String address;
    private final String containerHostAddress;
    private final int port;
    private final String targetPortExpression;

    /**
     * Initializes a new instance of the {@code AllocatedEndpoint} class.
     *
     * @param endpoint            The endpoint.
     * @param address             The IP address of the endpoint.
     * @param port                The port number of the endpoint.
     * @param containerHostAddress The address of the container host.
     * @param targetPortExpression A string representing how to retrieve the target port of the {@code AllocatedEndpoint} instance.
     */
    public AllocatedEndpoint(EndpointAnnotation endpoint, String address, int port, String containerHostAddress, String targetPortExpression) {
        Objects.requireNonNull(endpoint, "Endpoint cannot be null");
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 1 and 65535");
        }

        this.endpoint = endpoint;
        this.address = address;
        this.containerHostAddress = containerHostAddress;
        this.port = port;
        this.targetPortExpression = targetPortExpression;
    }

    public EndpointAnnotation getEndpoint() {
        return endpoint;
    }

    public String getAddress() {
        return address;
    }

    public String getContainerHostAddress() {
        return containerHostAddress;
    }

    public int getPort() {
        return port;
    }

    public String getUriScheme() {
        return endpoint.getUriScheme().toString();
    }

    public String getEndPointString() {
        return address + ":" + port;
    }

    public String getUriString() {
        return getUriScheme() + "://" + getEndPointString();
    }

    public String getTargetPortExpression() {
        return targetPortExpression;
    }

    @Override
    public String toString() {
        return getUriString();
    }
}
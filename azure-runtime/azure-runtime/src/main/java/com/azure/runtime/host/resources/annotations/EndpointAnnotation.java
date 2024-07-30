package com.azure.runtime.host.resources.annotations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.azure.runtime.host.resources.properties.AllocatedEndpoint;
import com.azure.runtime.host.resources.properties.Protocol;
import com.azure.runtime.host.resources.properties.Scheme;
import com.azure.runtime.host.resources.properties.Transport;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * Represents an endpoint annotation that describes how a service should be bound to a network.
 * <p>
 * This class is used to specify the network protocol, port, URI scheme, transport, and other details for a service.
 */
@JsonPropertyOrder({"scheme", "protocol", "transport", "external", "targetPort", "port"})
public class EndpointAnnotation implements ResourceAnnotation {

    @NotNull(message = "EndpointAnnotation.transport cannot be null")
    @JsonProperty("transport")
    private Transport transport;

    @JsonIgnore
    private final String name;

    @NotNull(message = "EndpointAnnotation.protocol cannot be null")
    @JsonProperty("protocol")
    private final Protocol protocol;

    @JsonProperty("port")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Min(value = 0, message = "EndpointAnnotation.port must be between 0 and 65535")
    @Max(value = 65535, message = "EndpointAnnotation.port must be between 0 and 65535")
    private Integer port;

    @JsonProperty("targetPort")
    @Min(value = 0, message = "EndpointAnnotation.targetPort must be between 0 and 65535")
    @Max(value = 65535, message = "EndpointAnnotation.targetPort must be between 0 and 65535")
    private Integer targetPort;

    @NotNull(message = "EndpointAnnotation.scheme cannot be null")
    @JsonProperty("scheme")
    private final Scheme uriScheme;

    @JsonProperty("external")
    private boolean external;

    @JsonIgnore
    private boolean proxied = true; // Default value is true

    @JsonIgnore
    private AllocatedEndpoint allocatedEndpoint;

    /**
     * Initializes a new instance of {@code EndpointAnnotation}.
     *
     * @param protocol   Network protocol: TCP or UDP are supported today, others possibly in future.
     * @param uriScheme  If a service is URI-addressable, this is the URI scheme to use for constructing service URI.
     * @param transport  Transport that is being used (e.g., http, http2, http3 etc.).
     * @param name       Name of the service.
     * @param port       Desired port for the service.
     * @param targetPort This is the port the resource is listening on. If the endpoint is used for the container, it is the container port.
     * @param isExternal Indicates that this endpoint should be exposed externally at publish time.
     * @param isProxied  Specifies if the endpoint will be proxied. Defaults to true.
     */
    public EndpointAnnotation(Protocol protocol, Scheme uriScheme, Transport transport, String name, Integer port,
                              Integer targetPort, boolean isExternal, boolean isProxied) {
        this.uriScheme = Objects.requireNonNull(uriScheme);
        this.name = Objects.requireNonNull(name);

        // ModelName.validateName("EndpointAnnotation", name); // Assuming ModelName.validateName is a method to validate the name

        this.protocol = protocol;
        this.transport = transport;
        this.port = port;
        this.targetPort = targetPort;

        // If the target port was not explicitly set and the service is not being proxied,
        // we can set the target port to the port.
        if (this.targetPort == null && !isProxied) {
            this.targetPort = port;
        }

        this.external = isExternal;
        this.proxied = isProxied;
    }

    public String getName() {
        return name;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(Integer targetPort) {
        this.targetPort = targetPort;
    }

    public Scheme getUriScheme() {
        return uriScheme;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        external = external;
    }

    @JsonIgnore
    public boolean isProxied() {
        return proxied;
    }

    public void setProxied(boolean proxied) {
        this.proxied = proxied;
    }

    public AllocatedEndpoint getAllocatedEndpoint() {
        return allocatedEndpoint;
    }

    public void setAllocatedEndpoint(AllocatedEndpoint allocatedEndpoint) {
        this.allocatedEndpoint = allocatedEndpoint;
    }

    @Override
    public String toString() {
        return "EndpointAnnotation{" +
            "name='" + name + '\'' +
            ", transport=" + transport +
            ", protocol=" + protocol +
            ", port=" + port +
            ", targetPort=" + targetPort +
            ", uriScheme=" + uriScheme +
            '}';
    }
}
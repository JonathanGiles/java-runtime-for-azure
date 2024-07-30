package com.azure.runtime.host.resources.traits;

import com.azure.runtime.host.resources.Resource;
import com.azure.runtime.host.resources.properties.Scheme;
import com.azure.runtime.host.resources.properties.Transport;
import com.azure.runtime.host.resources.references.EndpointReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.azure.runtime.host.implementation.ResourceUtilities;

import java.util.List;
import java.util.stream.Collectors;

public interface ResourceWithEndpoints<T extends Resource<T> & ResourceWithEndpoints<T>> extends ResourceTrait<T> {

    // TODO enable these
//    default T withEndpoint(EndpointReference<?> endpointReference) {
//        ResourceUtilities.withEndpoint((Resource<?>) self(), endpointReference);
//        return self();
//    }

    // FIXME javadoc
    /**
     * Exposes an HTTP endpoint on a resource. This endpoint reference can be retrieved using
     * GetEndpoint<T>(IResourceBuilder<T>, String). The endpoint name will be "http" if not specified.
     * @param targetPort This is the port the resource is listening on. If the endpoint is used for the container, it
     *                   is the container port.
     * @return
     */
    default T withHttpEndpoint(int targetPort) {
        return withHttpEndpoint(null, targetPort, "http", null, true);
    }

    // FIXME javadoc
    /**
     * Exposes an HTTPS endpoint on a resource. This endpoint reference can be retrieved using
     * GetEndpoint<T>(IResourceBuilder<T>, String). The endpoint name will be "https" if not specified.
     * @param targetPort
     * @return
     */
    default T withHttpsEndpoint(int targetPort) {
        return withHttpsEndpoint(null, targetPort, "https", null, true);
    }

    /**
     * Adds an HTTP endpoint to the resource with specified configurations.
     * <p>
     * This method configures an HTTP endpoint with the provided port, target port, name, environment variable, and
     * proxy settings. It's a convenience method for quickly adding a common HTTP endpoint to a resource.
     * </p>
     *
     * @param port          An optional host port.
     * @param targetPort    The port the resource is listening on. If the endpoint is used for the container, it is the
     *                      container port.
     * @param endpointName  The name of the endpoint. An optional parameter, defaults to "http" if not specified.
     * @param env           The name of the environment variable to inject. An optional parameter.
     * @param isProxied     Specifies if the endpoint will be proxied by DCP. Defaults to true.
     * @return The resource with the added HTTP endpoint.
     */
    default T withHttpEndpoint(Integer port, int targetPort, String endpointName, String env, boolean isProxied) {
        ResourceUtilities.withEndpoint(self(), Transport.HTTP, Scheme.HTTP, endpointName, port, targetPort, true, isProxied, env);
        return self();
    }

    /**
     * Exposes an HTTPS endpoint on a resource. This endpoint reference can be retrieved using the getEndpoint method.
     * The endpoint name will be "https" if not specified.
     *
     * @param port          An optional host port.
     * @param targetPort    This is the port the resource is listening on. If the endpoint is used for the container, it is the container port.
     * @param endpointName  An optional name of the endpoint. Defaults to "https" if not specified.
     * @param env           An optional name of the environment variable to inject.
     * @param isProxied     Specifies if the endpoint will be proxied by DCP. Defaults to true.
     * @return The resource with the added HTTPS endpoint.
     */
    default T withHttpsEndpoint(Integer port, int targetPort, String endpointName, String env, boolean isProxied) {
        ResourceUtilities.withEndpoint(self(), Transport.HTTP, Scheme.HTTPS, endpointName, port, targetPort, true, isProxied, env);
        return self();
    }

    @JsonIgnore
    default List<EndpointReference<?>> getEndpoints() {
        return ResourceUtilities.getEndpointAnnotations(self()).stream()
            .map(endpointAnnotation -> new EndpointReference<>(self(), endpointAnnotation.getName()))
            .collect(Collectors.toList());
    }

    /**
     * Gets an EndpointReference by name from the resource.
     * @param name The name of the endpoint.
     * @return
     */
    default EndpointReference<?> getEndpoint(String name) {
        return new EndpointReference<>(self(), name);
    }

    /**
     * Marks existing http or https endpoints on a resource as external.
     * @return The resource with updated endpoints.
     */
    default T withExternalHttpEndpoints() {
        ResourceUtilities.getEndpointAnnotationsAsStream((Resource<?>) self())
            .filter(endpointAnnotation -> {
                return endpointAnnotation.getUriScheme() == Scheme.HTTP || endpointAnnotation.getUriScheme() == Scheme.HTTPS;
            })
            .forEach(endpointAnnotation -> endpointAnnotation.setExternal(true));

        return self();
    }

    @Override
    @SuppressWarnings("unchecked")
    default T self() {
        return (T) this;
    }

    /*
    // TODO This is C# API to consider including...
    /// <summary>
    /// Changes an existing creates a new endpoint if it doesn't exist and invokes callback to modify the defaults.
    /// </summary>
    /// <param name="builder">Resource builder for resource with endpoints.</param>
    /// <param name="endpointName">Name of endpoint to change.</param>
    /// <param name="callback">Callback that modifies the endpoint.</param>
    /// <param name="createIfNotExists">Create endpoint if it does not exist.</param>
    /// <returns></returns>
    public static IResourceBuilder<T> WithEndpoint<T>(this IResourceBuilder<T> builder, string endpointName, Action<EndpointAnnotation> callback, bool createIfNotExists = true) where T : IResourceWithEndpoints
    {
        var endpoint = builder.Resource.Annotations
            .OfType<EndpointAnnotation>()
            .Where(ea => StringComparers.EndpointAnnotationName.Equals(ea.Name, endpointName))
            .SingleOrDefault();

        if (endpoint != null)
        {
            callback(endpoint);
        }

        if (endpoint == null && createIfNotExists)
        {
            endpoint = new EndpointAnnotation(ProtocolType.Tcp, name: endpointName);
            callback(endpoint);
            builder.Resource.Annotations.Add(endpoint);
        }
        else if (endpoint == null && !createIfNotExists)
        {
            return builder;
        }

        return builder;
    }



    /// <summary>
    /// Configures a resource to mark all endpoints' transport as HTTP/2. This is useful for HTTP/2 services that need prior knowledge.
    /// </summary>
    /// <typeparam name="T">The resource type.</typeparam>
    /// <param name="builder">The resource builder.</param>
    /// <returns>The <see cref="IResourceBuilder{T}"/>.</returns>
    public static IResourceBuilder<T> AsHttp2Service<T>(this IResourceBuilder<T> builder) where T : IResourceWithEndpoints
    {
        return builder.WithAnnotation(new Http2ServiceAnnotation());
    }
     */
}

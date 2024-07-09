package com.microsoft.aspire.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.aspire.implementation.json.CustomSerialize;
import com.microsoft.aspire.implementation.json.ResourceSerializer;
import com.microsoft.aspire.resources.traits.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @param <T>
 */
@JsonPropertyOrder({"type", "params"})
@CustomSerialize(serializer = ResourceSerializer.class)
public abstract class Resource<T extends Resource<T>> implements ResourceWithLifecycle, SelfAware<T> {

    @Valid
    @NotNull(message = "Resource Type cannot be null")
    @JsonProperty("type")
    private final ResourceType type;

    // the name of the resource
    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be an empty string")
    @JsonIgnore
    private final String name;

    @JsonIgnore
    private final List<ResourceAnnotation> annotations;

    public Resource(ResourceType type, String name) {
        this.type = type;
        this.name = name;
        this.annotations = new ArrayList<>();
    }

    public final ResourceType getType() {
        return type;
    }

    public final String getName() {
        return name;
    }

    /**
     * Returns an unmodifiable list of annotations associated with the resource.
     * @return The list of annotations.
     */
    public final List<ResourceAnnotation> getAnnotations() {
        return Collections.unmodifiableList(annotations);
    }

    public final T withAnnotation(ResourceAnnotation annotation) {
        annotations.add(annotation);
        return self();
    }

    public void copyInto(Resource<?> newResource) {
        // TODO this is incomplete, and I'm not sure we should keep it!
        // look at the traits of this resource, and copy them into the new resource
        if (this instanceof ResourceWithArguments<?> oldResource && newResource instanceof ResourceWithArguments<?> _newResource) {
            oldResource.getArguments().forEach(_newResource::withArgument);
        }
//        if (this instanceof ResourceWithEndpoints<?> oldResource && newResource instanceof ResourceWithEndpoints<?> _newResource) {
//            rwe.getEndpoints().forEach(nrwe::withEndpoint);
//        }
//        if (this instanceof ResourceWithBindings<?> oldResource && newResource instanceof ResourceWithBindings<?> _newResource) {
//            oldResource.getBindings().values().forEach(_newResource::withBinding);
//        }
//        if (this instanceof ResourceWithConnectionString<?> oldResource && newResource instanceof ResourceWithConnectionString<?> _newResource) {
//            oldResource.getBindings().values().forEach(_newResource::withBinding);
//        }
        if (this instanceof ResourceWithParameters<?> oldResource && newResource instanceof ResourceWithParameters<?> _newResource) {
            oldResource.getParameters().forEach(_newResource::withParameter);
        }
//        if (this instanceof ResourceWithEnvironment<?> oldResource && newResource instanceof ResourceWithEnvironment<?> _newResource) {
//            oldResource.getEnvironment().forEach(_newResource::withEnvironment);
//        }
    }

    /*

    /// <summary>
    /// Exposes an HTTP endpoint on a resource. This endpoint reference can be retrieved using <see cref="ResourceBuilderExtensions.GetEndpoint{T}(IResourceBuilder{T}, string)"/>.
    /// The endpoint name will be "http" if not specified.
    /// </summary>
    /// <typeparam name="T">The resource type.</typeparam>
    /// <param name="builder">The resource builder.</param>
    /// <param name="targetPort">This is the port the resource is listening on. If the endpoint is used for the container, it is the container port.</param>
    /// <param name="port">An optional port. This is the port that will be given to other resources to communicate with this resource.</param>
    /// <param name="name">An optional name of the endpoint. Defaults to "http" if not specified.</param>
    /// <param name="env">An optional name of the environment variable to inject.</param>
    /// <param name="isProxied">Specifies if the endpoint will be proxied by DCP. Defaults to true.</param>
    /// <returns>The <see cref="IResourceBuilder{T}"/>.</returns>
    /// <exception cref="DistributedApplicationException">Throws an exception if an endpoint with the same name already exists on the specified resource.</exception>
    public static IResourceBuilder<T> WithHttpEndpoint<T>(this IResourceBuilder<T> builder, int? port = null, int? targetPort = null, string? name = null, string? env = null, bool isProxied = true) where T : IResource
    {
        return builder.WithEndpoint(targetPort: targetPort, port: port, scheme: "http", name: name, env: env, isProxied: isProxied);
    }

    /// <summary>
    /// Exposes an HTTPS endpoint on a resource. This endpoint reference can be retrieved using <see cref="ResourceBuilderExtensions.GetEndpoint{T}(IResourceBuilder{T}, string)"/>.
    /// The endpoint name will be "https" if not specified.
    /// </summary>
    /// <typeparam name="T">The resource type.</typeparam>
    /// <param name="builder">The resource builder.</param>
    /// <param name="targetPort">This is the port the resource is listening on. If the endpoint is used for the container, it is the container port.</param>
    /// <param name="port">An optional host port.</param>
    /// <param name="name">An optional name of the endpoint. Defaults to "https" if not specified.</param>
    /// <param name="env">An optional name of the environment variable to inject.</param>
    /// <param name="isProxied">Specifies if the endpoint will be proxied by DCP. Defaults to true.</param>
    /// <returns>The <see cref="IResourceBuilder{T}"/>.</returns>
    /// <exception cref="DistributedApplicationException">Throws an exception if an endpoint with the same name already exists on the specified resource.</exception>
    public static IResourceBuilder<T> WithHttpsEndpoint<T>(this IResourceBuilder<T> builder, int? port = null, int? targetPort = null, string? name = null, string? env = null, bool isProxied = true) where T : IResource
    {
        return builder.WithEndpoint(targetPort: targetPort, port: port, scheme: "https", name: name, env: env, isProxied: isProxied);
    }
     */
}

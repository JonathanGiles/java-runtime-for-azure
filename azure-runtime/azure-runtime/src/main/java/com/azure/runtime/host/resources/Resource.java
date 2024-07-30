package com.azure.runtime.host.resources;

import com.azure.runtime.host.resources.annotations.ResourceAnnotation;
import com.azure.runtime.host.resources.traits.ResourceWithLifecycle;
import com.azure.runtime.host.resources.traits.ResourceWithParameters;
import com.azure.runtime.host.resources.traits.SelfAware;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.azure.runtime.host.utils.json.CustomSerialize;
import com.azure.runtime.host.implementation.utils.json.ResourceSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic resource within the Java Runtime for Azure framework. This abstract class serves as the foundation
 * for all specific types of resources that can be part of a distributed application, such as containers, executables, values,
 * and more. Each resource is characterized by a unique type and name, and can be annotated with additional metadata
 * to provide further context or configuration.
 * <p>
 * Resources are the building blocks of a Java Runtime for Azure application, allowing developers to define the components that
 * make up their application in a structured and extensible manner. This class provides common functionality that
 * all resources share, including lifecycle management, self-awareness for fluent API design, and annotation support.
 * <p>
 * Usage example:
 *
 * {@snippet lang="java" :
 * // Define a new DockerFile resource
 * DockerFile dockerFile = new DockerFile("MyDockerFile", "./Dockerfile", ".");
 * // Add the DockerFile to the application
 * DistributedApplication app = DistributedApplication.getInstance();
 * app.addResource(dockerFile);
 * }
 *
 * @param <T> The specific type of the resource, which may or may not be a subtype of this class. This allows for
 *           method chaining, even when using a subtype, when used in conjunction with the API on
 *           {@link SelfAware}.
 * @see ResourceType
 * @see ResourceWithLifecycle
 * @see SelfAware
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

    /**
     * Gets the type of this resource. The type is used to categorize resources within the Java Runtime for Azure framework,
     * facilitating type-specific handling and configuration.
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * ResourceType type = resource.getType();
     * System.out.println("Resource type: " + type);
     * }
     *
     * @return The {@link ResourceType} of this resource.
     */
    public final ResourceType getType() {
        return type;
    }

    /**
     * Gets the name of this resource. The name is a unique identifier within the context of the application,
     * allowing for easy reference and management of the resource.
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * String name = resource.getName();
     * System.out.println("Resource name: " + name);
     * }
     *
     * @return The name of this resource.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns a modifiable list of annotations associated with this resource. Annotations can be used to attach
     * additional metadata or configuration to a resource, enhancing its functionality or altering its behavior.
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * resource.getAnnotations().add(new ResourceAnnotation("key", "value"));
     * }
     *
     * @return A list of {@link ResourceAnnotation} objects associated with this resource.
     */
    public final List<ResourceAnnotation> getAnnotations() {
        return annotations;
    }

    /**
     * Adds an annotation to this resource. This method provides a fluent interface for adding annotations,
     * allowing for easy chaining of configuration methods.
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * resource.withAnnotation(new ResourceAnnotation("key", "value"));
     * }
     *
     * @param annotation The annotation to add to this resource.
     * @return This resource, to allow for method chaining.
     */
    public final T withAnnotation(ResourceAnnotation annotation) {
        annotations.add(annotation);
        return self();
    }

    /**
     * Copies the characteristics and annotations of this resource into another resource. This method is useful
     * for duplicating or templating resources within an application. Note that this method is currently incomplete
     * and may not copy all aspects of the resource. It is recommended to review the implementation before use.
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * Resource<?> newResource = new SomeResourceType(...);
     * existingResource.copyInto(newResource);
     * }
     *
     * @param newResource The resource into which the characteristics and annotations of this resource will be copied.
     */
    public void copyInto(Resource<?> newResource) {
        // TODO this is incomplete, and I'm not sure we should keep it!
        // look at the traits of this resource, and copy them into the new resource

        // copy all annotations into the new resource
        newResource.getAnnotations().addAll(getAnnotations());

//        if (this instanceof ResourceWithArguments<?> oldResource && newResource instanceof ResourceWithArguments<?> _newResource) {
//            oldResource.getArguments().forEach(_newResource::withArgument);
//        }
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
    // TODO consider the following
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

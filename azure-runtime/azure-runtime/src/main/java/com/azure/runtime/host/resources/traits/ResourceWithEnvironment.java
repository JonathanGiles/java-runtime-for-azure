package com.azure.runtime.host.resources.traits;

import com.azure.runtime.host.implementation.ResourceUtilities;
import com.azure.runtime.host.implementation.TemplateStrings;
import com.azure.runtime.host.resources.Resource;
import com.azure.runtime.host.resources.references.EndpointReference;
import com.azure.runtime.host.resources.annotations.EnvironmentCallbackAnnotation;
import com.azure.runtime.host.resources.annotations.EnvironmentCallbackContext;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public interface ResourceWithEnvironment<T extends Resource<T> & ResourceWithEnvironment<T>> extends ResourceTrait<T> {

    default T withEnvironment(String key, String value) {
        ResourceUtilities.withEnvironment(self(), key, value);
        return self();
    }

    default T withEnvironment(Map<String, String> environment) {
        for (Map.Entry<String, String> entry : environment.entrySet()) {
            withEnvironment(entry.getKey(), entry.getValue());
        }
        return self();
    }

    /**
     * Injects service discovery information as environment variables from the URI into the destination resource,
     * using the name as the service name. The URI will be injected using the format "services__{name}__default__0={uri}."
     *
     * @param name    The name of the service.
     * @param url     The URL of the service.
     * @return The resource configured with the specified reference.
     * @throws MalformedURLException If the URI is not absolute or if the absolute path is not "/".
     */
    default T withReference(String name, String url) throws MalformedURLException {
        URL _url = URI.create(url).toURL();

        if (_url.getProtocol() == null) {
            throw new MalformedURLException("The URL must have a protocol, indicating it is absolute.");
        }

        if (!_url.getPath().equals("/")) {
            throw new MalformedURLException("The URL absolute path must be \"/\".");
        }

        withEnvironment(TemplateStrings.evaluateService(name), _url.toString());

        return self();
    }


    /**
     * Injects service discovery information from the specified endpoint into the resource using the source
     * resource's name as the service name. Each endpoint will be injected using the format
     * "services__{sourceResourceName}__{endpointName}__{endpointIndex}={uriString}."
     *
     * @param endpointReference The endpoint from which to extract the URL.
     * @return The resource configured with the specified reference.
     */
    default <R extends Resource<?> & ResourceWithEnvironment<?>> T withReference(EndpointReference<?> endpointReference) {
        ResourceUtilities.applyEndpoints(self(), endpointReference.getResource(), endpointReference.getEndpointName());
        return self();
    }

//    /**
//     * Injects service discovery information as environment variables from the project resource into the destination resource,
//     * using the source resource's name as the service name. Each endpoint defined on the project resource will be injected
//     * using the format "services__{sourceResourceName}__{endpointName}__{endpointIndex}={uriString}."
//     *
//     * @param builder The resource builder where the service discovery information will be injected.
//     * @param source  The resource builder from which to extract service discovery information.
//     * @param <TDestination> The type of the destination resource, which must implement ResourceWithEnvironment.
//     * @return The builder instance for chaining.
//     */
//    public static <TDestination extends ResourceWithEnvironment<TDestination>> ResourceBuilder<TDestination> withReference(
//        ResourceBuilder<TDestination> builder, ResourceBuilder<? extends ResourceWithServiceDiscovery> source) {
//
//        ResourceWithServiceDiscovery serviceDiscoveryResource = source.getResource();
//        serviceDiscoveryResource.getEndpoints().forEach((endpointName, endpoint) -> {
//            for (int i = 0; i < endpoint.getUris().size(); i++) {
//                String envVarName = String.format("services__%s__%s__%d", serviceDiscoveryResource.getName(), endpointName, i);
//                String envVarValue = endpoint.getUris().get(i).toString();
//                builder.withEnvironment(envVarName, envVarValue);
//            }
//        });
//
//        return builder;
//    }

//    /**
//     * Injects a connection string as an environment variable from the source resource into the destination resource,
//     * using the source resource's name as the connection string name (if not overridden).
//     * The format of the environment variable will be "ConnectionStrings__{sourceResourceName}={connectionString}."
//     * <p>
//     * Each resource defines the format of the connection string value. The underlying connection string value can be
//     * retrieved using {@link ResourceWithConnectionString#getConnectionString()}.
//     * </p>
//     * <p>
//     * Connection strings are also resolved by the configuration system (appSettings.json in the AppHost project, or
//     * environment variables). If a connection string is not found on the resource, the configuration system will be
//     * queried for a connection string using the resource's name.
//     * </p>
//     * @param source The resource where connection string will be injected.
//     * @param source The resource from which to extract the connection string.
//     * @param connectionName An override of the source resource's name for the connection string. The resulting connection
//     *                       string will be "ConnectionStrings__connectionName" if this is not null.
//     * @param optional true to allow a missing connection string; false to throw an exception if the connection string is
//     *                 not found.
//     * @return A reference to the builder.
//     * @throws DistributedApplicationException Throws an exception if the connection string resolves to null. It can be null
//     *                                         if the resource has no connection string, and if the configuration has no
//     *                                         connection string for the source resource.
//     */
//    default <R extends Resource<?> & ResourceWithConnectionString<?>> T withReference(R source) {
//        return withReference(source, null, false);
//    }

    /**
     * Injects a connection string as an environment variable from the source resource into the destination resource,
     * using the source resource's name as the connection string name (if not overridden).
     * The format of the environment variable will be "ConnectionStrings__{sourceResourceName}={connectionString}."
     * <p>
     * Each resource defines the format of the connection string value. The underlying connection string value can be
     * retrieved using {@link ResourceWithConnectionString#getConnectionString()}.
     * </p>
     * <p>
     * Connection strings are also resolved by the configuration system (appSettings.json in the AppHost project, or
     * environment variables). If a connection string is not found on the resource, the configuration system will be
     * queried for a connection string using the resource's name.
     * </p>
     *
     * @param source The resource from which to extract the connection string.
     * @param connectionName An override of the source resource's name for the connection string. The resulting connection
     *                       string will be "ConnectionStrings__connectionName" if this is not null.
     * @param optional true to allow a missing connection string; false to throw an exception if the connection string is
     *                 not found.
     * @return The resource configured with the specified reference.
     */
    default <R extends Resource<?> & ResourceWithConnectionString<?>> T withReference(
        R source, String connectionName, boolean optional) {

        String resolvedConnectionName = Optional.ofNullable(connectionName).orElse(source.getName());
        String connectionStringName = Optional.ofNullable(source.getConnectionStringEnvironmentVariable())
            .orElse(TemplateStrings.evaluateConnectionString(resolvedConnectionName));

        // Assuming there's a method to add environment variables in the builder
        withEnvironment(connectionStringName, source.getConnectionString(/*optional*/)); // FIXME maybe optional?

        return self();
    }

    default T withEnvironment(Consumer<EnvironmentCallbackContext> callback) {
        ResourceUtilities.applyAnnotation(self(), new EnvironmentCallbackAnnotation("Unknown callback", callback));
        return self();
    }


    /*
    // TODO This is C# API to consider including...
    /// <summary>
    /// Adds an environment variable to the resource.
    /// </summary>
    /// <typeparam name="T">The resource type.</typeparam>
    /// <param name="builder">The resource builder.</param>
    /// <param name="name">The name of the environment variable.</param>
    /// <param name="value">The value of the environment variable.</param>
    /// <returns>A resource configured with the specified environment variable.</returns>
    public static IResourceBuilder<T> WithEnvironment<T>(this IResourceBuilder<T> builder, string name, in ReferenceExpression.ExpressionInterpolatedStringHandler value)
        where T : IResourceWithEnvironment
    {
        var expression = value.GetExpression();

        return builder.WithEnvironment(context =>
        {
            context.EnvironmentVariables[name] = expression;
        });
    }

    /// <summary>
    /// Adds an environment variable to the resource.
    /// </summary>
    /// <typeparam name="T">The resource type.</typeparam>
    /// <param name="builder">The resource builder.</param>
    /// <param name="name">The name of the environment variable.</param>
    /// <param name="value">The value of the environment variable.</param>
    /// <returns>A resource configured with the specified environment variable.</returns>
    public static IResourceBuilder<T> WithEnvironment<T>(this IResourceBuilder<T> builder, string name, ReferenceExpression value)
        where T : IResourceWithEnvironment
    {
        return builder.WithEnvironment(context =>
        {
            context.EnvironmentVariables[name] = value;
        });
    }

    /// <summary>
    /// Adds an environment variable to the resource.
    /// </summary>
    /// <typeparam name="T">The resource type.</typeparam>
    /// <param name="builder">The resource builder.</param>
    /// <param name="name">The name of the environment variable.</param>
    /// <param name="callback">A callback that allows for deferred execution of a specific environment variable. This runs after resources have been allocated by the orchestrator and allows access to other resources to resolve computed data, e.g. connection strings, ports.</param>
    /// <returns>A resource configured with the specified environment variable.</returns>
    public static IResourceBuilder<T> WithEnvironment<T>(this IResourceBuilder<T> builder, string name, Func<string> callback) where T : IResourceWithEnvironment
    {
        return builder.WithAnnotation(new EnvironmentCallbackAnnotation(name, callback));
    }

    /// <summary>
    /// Allows for the population of environment variables on a resource.
    /// </summary>
    /// <typeparam name="T">The resource type.</typeparam>
    /// <param name="builder">The resource builder.</param>
    /// <param name="callback">A callback that allows for deferred execution for computing many environment variables. This runs after resources have been allocated by the orchestrator and allows access to other resources to resolve computed data, e.g. connection strings, ports.</param>
    /// <returns>A resource configured with the environment variable callback.</returns>
    public static IResourceBuilder<T> WithEnvironment<T>(this IResourceBuilder<T> builder, Action<EnvironmentCallbackContext> callback) where T : IResourceWithEnvironment
    {
        return builder.WithAnnotation(new EnvironmentCallbackAnnotation(callback));
    }

    /// <summary>
    /// Allows for the population of environment variables on a resource.
    /// </summary>
    /// <typeparam name="T">The resource type.</typeparam>
    /// <param name="builder">The resource builder.</param>
    /// <param name="callback">A callback that allows for deferred execution for computing many environment variables. This runs after resources have been allocated by the orchestrator and allows access to other resources to resolve computed data, e.g. connection strings, ports.</param>
    /// <returns>A resource configured with the environment variable callback.</returns>
    public static IResourceBuilder<T> WithEnvironment<T>(this IResourceBuilder<T> builder, Func<EnvironmentCallbackContext, Task> callback) where T : IResourceWithEnvironment
    {
        return builder.WithAnnotation(new EnvironmentCallbackAnnotation(callback));
    }

    /// <summary>
    /// Adds an environment variable to the resource with the endpoint for <paramref name="endpointReference"/>.
    /// </summary>
    /// <typeparam name="T">The resource type.</typeparam>
    /// <param name="builder">The resource builder.</param>
    /// <param name="name">The name of the environment variable.</param>
    /// <param name="endpointReference">The endpoint from which to extract the url.</param>
    /// <returns>A resource configured with the environment variable callback.</returns>
    public static IResourceBuilder<T> WithEnvironment<T>(this IResourceBuilder<T> builder, string name, EndpointReference endpointReference) where T : IResourceWithEnvironment
    {
        return builder.WithEnvironment(context =>
        {
            context.EnvironmentVariables[name] = endpointReference;
        });
    }

    /// <summary>
    /// Adds an environment variable to the resource with the value from <paramref name="parameter"/>.
    /// </summary>
    /// <typeparam name="T">The resource type.</typeparam>
    /// <param name="builder">The resource builder.</param>
    /// <param name="name">Name of environment variable</param>
    /// <param name="parameter">Resource builder for the parameter resource.</param>
    /// <returns>A resource configured with the environment variable callback.</returns>
    public static IResourceBuilder<T> WithEnvironment<T>(this IResourceBuilder<T> builder, string name, IResourceBuilder<ParameterResource> parameter) where T : IResourceWithEnvironment
    {
        return builder.WithEnvironment(context =>
        {
            context.EnvironmentVariables[name] = parameter.Resource;
        });
    }

    /// <summary>
    /// Adds an environment variable to the resource with the connection string from the referenced resource.
    /// </summary>
    /// <typeparam name="T">The destination resource type.</typeparam>
    /// <param name="builder">The destination resource builder to which the environment variable will be added.</param>
    /// <param name="envVarName">The name of the environment variable under which the connection string will be set.</param>
    /// <param name="resource">The resource builder of the referenced service from which to pull the connection string.</param>
    /// <returns>The <see cref="IResourceBuilder{T}"/>.</returns>
    public static IResourceBuilder<T> WithEnvironment<T>(
        this IResourceBuilder<T> builder,
        string envVarName,
        IResourceBuilder<IResourceWithConnectionString> resource)
        where T : IResourceWithEnvironment
    {
        return builder.WithEnvironment(context =>
        {
            context.EnvironmentVariables[envVarName] = new ConnectionStringReference(resource.Resource, optional: false);
        });
    }




    /// <summary>
    /// Injects a connection string as an environment variable from the source resource into the destination resource, using the source resource's name as the connection string name (if not overridden).
    /// The format of the environment variable will be "ConnectionStrings__{sourceResourceName}={connectionString}."
    /// <para>
    /// Each resource defines the format of the connection string value. The
    /// underlying connection string value can be retrieved using <see cref="IResourceWithConnectionString.GetConnectionStringAsync(CancellationToken)"/>.
    /// </para>
    /// <para>
    /// Connection strings are also resolved by the configuration system (appSettings.json in the AppHost project, or environment variables). If a connection string is not found on the resource, the configuration system will be queried for a connection string
    /// using the resource's name.
    /// </para>
    /// </summary>
    /// <typeparam name="TDestination">The destination resource.</typeparam>
    /// <param name="builder">The resource where connection string will be injected.</param>
    /// <param name="source">The resource from which to extract the connection string.</param>
    /// <param name="connectionName">An override of the source resource's name for the connection string. The resulting connection string will be "ConnectionStrings__connectionName" if this is not null.</param>
    /// <param name="optional"><see langword="true"/> to allow a missing connection string; <see langword="false"/> to throw an exception if the connection string is not found.</param>
    /// <exception cref="DistributedApplicationException">Throws an exception if the connection string resolves to null. It can be null if the resource has no connection string, and if the configuration has no connection string for the source resource.</exception>
    /// <returns>A reference to the <see cref="IResourceBuilder{T}"/>.</returns>
    public static IResourceBuilder<TDestination> WithReference<TDestination>(this IResourceBuilder<TDestination> builder, IResourceBuilder<IResourceWithConnectionString> source, string? connectionName = null, bool optional = false)
        where TDestination : IResourceWithEnvironment
    {
        var resource = source.Resource;
        connectionName ??= resource.Name;

        return builder.WithEnvironment(context =>
        {
            var connectionStringName = resource.ConnectionStringEnvironmentVariable ?? $"{ConnectionStringEnvironmentName}{connectionName}";

            context.EnvironmentVariables[connectionStringName] = new ConnectionStringReference(resource, optional);
        });
    }

    /// <summary>
    /// Injects service discovery information as environment variables from the project resource into the destination resource, using the source resource's name as the service name.
    /// Each endpoint defined on the project resource will be injected using the format "services__{sourceResourceName}__{endpointName}__{endpointIndex}={uriString}."
    /// </summary>
    /// <typeparam name="TDestination">The destination resource.</typeparam>
    /// <param name="builder">The resource where the service discovery information will be injected.</param>
    /// <param name="source">The resource from which to extract service discovery information.</param>
    /// <returns>A reference to the <see cref="IResourceBuilder{T}"/>.</returns>
    public static IResourceBuilder<TDestination> WithReference<TDestination>(this IResourceBuilder<TDestination> builder, IResourceBuilder<IResourceWithServiceDiscovery> source)
        where TDestination : IResourceWithEnvironment
    {
        ApplyEndpoints(builder, source.Resource);
        return builder;
    }

    /// <summary>
    /// Injects service discovery information as environment variables from the uri into the destination resource, using the name as the service name.
    /// The uri will be injected using the format "services__{name}__default__0={uri}."
    /// </summary>
    /// <typeparam name="TDestination"></typeparam>
    /// <param name="builder">The resource where the service discovery information will be injected.</param>
    /// <param name="name">The name of the service.</param>
    /// <param name="uri">The uri of the service.</param>
    /// <returns>A reference to the <see cref="IResourceBuilder{T}"/>.</returns>
    public static IResourceBuilder<TDestination> WithReference<TDestination>(this IResourceBuilder<TDestination> builder, string name, Uri uri)
        where TDestination : IResourceWithEnvironment
    {
        if (!uri.IsAbsoluteUri)
        {
            throw new InvalidOperationException("The uri for service reference must be absolute.");
        }

        if (uri.AbsolutePath != "/")
        {
            throw new InvalidOperationException("The uri absolute path must be \"/\".");
        }

        return builder.WithEnvironment($"services__{name}__default__0", uri.ToString());
    }

    /// <summary>
    /// Injects service discovery information from the specified endpoint into the project resource using the source resource's name as the service name.
    /// Each endpoint will be injected using the format "services__{sourceResourceName}__{endpointName}__{endpointIndex}={uriString}."
    /// </summary>
    /// <typeparam name="TDestination">The destination resource.</typeparam>
    /// <param name="builder">The resource where the service discovery information will be injected.</param>
    /// <param name="endpointReference">The endpoint from which to extract the url.</param>
    /// <returns>A reference to the <see cref="IResourceBuilder{T}"/>.</returns>
    public static IResourceBuilder<TDestination> WithReference<TDestination>(this IResourceBuilder<TDestination> builder, EndpointReference endpointReference)
        where TDestination : IResourceWithEnvironment
    {
        ApplyEndpoints(builder, endpointReference.Resource, endpointReference.EndpointName);
        return builder;
    }

    private static void ApplyEndpoints<T>(this IResourceBuilder<T> builder, IResourceWithEndpoints resourceWithEndpoints, string? endpointName = null)
        where T : IResourceWithEnvironment
    {
        // When adding an endpoint we get to see whether there is an EndpointReferenceAnnotation
        // on the resource, if there is then it means we have already been here before and we can just
        // skip this and note the endpoint that we want to apply to the environment in the future
        // in a single pass. There is one EndpointReferenceAnnotation per endpoint source.
        var endpointReferenceAnnotation = builder.Resource.Annotations
            .OfType<EndpointReferenceAnnotation>()
            .Where(sra => sra.Resource == resourceWithEndpoints)
            .SingleOrDefault();

        if (endpointReferenceAnnotation == null)
        {
            endpointReferenceAnnotation = new EndpointReferenceAnnotation(resourceWithEndpoints);
            builder.WithAnnotation(endpointReferenceAnnotation);

            var callback = CreateEndpointReferenceEnvironmentPopulationCallback(endpointReferenceAnnotation);
            builder.WithEnvironment(callback);
        }

        // If no specific endpoint name is specified, go and add all the endpoints.
        if (endpointName == null)
        {
            endpointReferenceAnnotation.UseAllEndpoints = true;
        }
        else
        {
            endpointReferenceAnnotation.EndpointNames.Add(endpointName);
        }
    }

     */
}

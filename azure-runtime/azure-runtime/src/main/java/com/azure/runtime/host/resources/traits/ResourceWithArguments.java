package com.azure.runtime.host.resources.traits;

import com.azure.runtime.host.resources.Resource;
import com.azure.runtime.host.implementation.ResourceUtilities;
import com.azure.runtime.host.resources.annotations.ArgsAnnotation;

public interface ResourceWithArguments<T extends Resource<T> & ResourceWithArguments<T>>  extends ResourceTrait<T> {

    default T withArgument(String argument) {
        ResourceUtilities.applyAnnotation(self(), ArgsAnnotation.createArgs(argument));
        return self();
    }

    default T withArguments(String... arguments) {
        for (String argument : arguments) {
            withArgument(argument);
        }

        return self();
    }

    default T withArguments(Iterable<String> arguments) {
        for (String argument : arguments) {
            withArgument(argument);
        }
        return self();
    }

    /*
    // TODO This is C# API to consider including...
    /// <summary>
    /// Adds the arguments to be passed to a container resource when the container is started.
    /// </summary>
    /// <typeparam name="T">The resource type.</typeparam>
    /// <param name="builder">The resource builder.</param>
    /// <param name="args">The arguments to be passed to the container when it is started.</param>
    /// <returns>The <see cref="IResourceBuilder{T}"/>.</returns>
    public static IResourceBuilder<T> WithArgs<T>(this IResourceBuilder<T> builder, params string[] args) where T : IResourceWithArgs
    {
        return builder.WithArgs(context => context.Args.AddRange(args));
    }

    /// <summary>
    /// Adds a callback to be executed with a list of command-line arguments when a container resource is started.
    /// </summary>
    /// <typeparam name="T"></typeparam>
    /// <param name="builder">The resource builder.</param>
    /// <param name="callback">A callback that allows for deferred execution for computing arguments. This runs after resources have been allocated by the orchestrator and allows access to other resources to resolve computed data, e.g. connection strings, ports.</param>
    /// <returns>The <see cref="IResourceBuilder{T}"/>.</returns>
    public static IResourceBuilder<T> WithArgs<T>(this IResourceBuilder<T> builder, Action<CommandLineArgsCallbackContext> callback) where T : IResourceWithArgs
    {
        return builder.WithArgs(context =>
        {
            callback(context);
            return Task.CompletedTask;
        });
    }

    /// <summary>
    /// Adds a callback to be executed with a list of command-line arguments when a container resource is started.
    /// </summary>
    /// <typeparam name="T">The resource type.</typeparam>
    /// <param name="builder">The resource builder.</param>
    /// <param name="callback">A callback that allows for deferred execution for computing arguments. This runs after resources have been allocated by the orchestrator and allows access to other resources to resolve computed data, e.g. connection strings, ports.</param>
    /// <returns>The <see cref="IResourceBuilder{T}"/>.</returns>
    public static IResourceBuilder<T> WithArgs<T>(this IResourceBuilder<T> builder, Func<CommandLineArgsCallbackContext, Task> callback) where T : IResourceWithArgs
    {
        return builder.WithAnnotation(new CommandLineArgsCallbackAnnotation(callback));
    }
     */
}

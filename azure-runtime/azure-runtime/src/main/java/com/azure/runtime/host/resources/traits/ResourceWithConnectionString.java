package com.azure.runtime.host.resources.traits;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.azure.runtime.host.resources.Resource;
import com.azure.runtime.host.resources.references.ReferenceExpression;

import java.util.List;

public interface ResourceWithConnectionString<T extends Resource<T> & ResourceWithConnectionString<T>>
    extends ResourceTrait<T>, ManifestExpressionProvider, ValueProvider, ValueWithReferences {

    @Override
    default String getValue() {
        return getConnectionString();
    }

    /**
     * Gets the connection string associated with the resource.
     * @return
     */
    @JsonIgnore
    default String getConnectionString() {
        return getConnectionStringExpression().getValue();
    }

    /**
     * The environment variable name to use for the connection string.
     * @return
     */
    @JsonIgnore
    default String getConnectionStringEnvironmentVariable() {
        return null;
    }

    /**
     * Describes the connection string format string used for this resource.
     * @return
     */
    @JsonIgnore
    ReferenceExpression getConnectionStringExpression();

    default List<Object> getReferences() {
        return List.of(getConnectionStringExpression());
    }

//    /**
//     * An override of the source resource's name for the connection string. The resulting connection string will be
//     * "ConnectionStrings__connectionName" if this is not null.
//     */
//    @JsonIgnore
//    default String getConnectionName() {
//        return null;
//    }

    /*
    // TODO This is C# API to consider including...
    /// <summary>
    /// Registers a callback which is invoked when a connection string is requested for a resource.
    /// </summary>
    /// <typeparam name="T">The resource type.</typeparam>
    /// <param name="builder">The resource builder.</param>
    /// <param name="resource">Resource to which connection string generation is redirected.</param>
    /// <returns>A reference to the <see cref="IResourceBuilder{T}"/>.</returns>
    public static IResourceBuilder<T> WithConnectionStringRedirection<T>(this IResourceBuilder<T> builder, IResourceWithConnectionString resource) where T : IResourceWithConnectionString
    {
        // You can only ever have one manifest publishing callback, so it must be a replace operation.
        return builder.WithAnnotation(new ConnectionStringRedirectAnnotation(resource), ResourceAnnotationMutationBehavior.Replace);
    }
     */
}

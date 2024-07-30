package com.azure.runtime.host.resources.traits;

import com.azure.runtime.host.implementation.ResourceUtilities;
import com.azure.runtime.host.implementation.TemplateStrings;
import com.azure.runtime.host.resources.Resource;
import com.azure.runtime.host.resources.references.ConnectionStringReference;
import com.azure.runtime.host.resources.annotations.EnvironmentCallbackAnnotation;

public interface ResourceWithReference<T extends Resource<T> & ResourceWithReference<T>> extends ResourceTrait<T> {

    default T withReference(Resource<?> resource) {
        // https://learn.microsoft.com/en-us/dotnet/api/aspire.hosting.resourcebuilderextensions.withreference?view=dotnet-aspire-8.0.1#aspire-hosting-resourcebuilderextensions-withreference-1(aspire-hosting-applicationmodel-iresourcebuilder((-0))-aspire-hosting-applicationmodel-iresourcebuilder((aspire-hosting-applicationmodel-iresourcewithconnectionstring))-system-string-system-boolean)

//        // we are adding references from this resource, to the given resource. We do this by adding appropriate
//        // properties (such as environment variables) to this resource, that will allow it to connect to the given
//        // resource. The kind and value of the properties we add will depend on the type of the given resource,
//        // and we base this on the traits that the given resource possesses.
//
//        // for now we only do this with environment variables, so we look to ensure that 'this' in a ResourceWithEnvironment
        if (! (this instanceof ResourceWithEnvironment<?>)) {
            throw new UnsupportedOperationException("Cannot add reference to a resource that does not support environment variables");
        }

        Resource<?> thisResource = (Resource<?>) this;
        ResourceWithEnvironment<?> thisResourceWithEnvironment = (ResourceWithEnvironment<?>) this;

        if (resource instanceof ResourceWithConnectionString<?> rwcs) {
            boolean optional = false;
            final String connectionName = resource.getName();

            String connectionStringName = rwcs.getConnectionStringEnvironmentVariable();
            if (connectionStringName == null) {
                connectionStringName = TemplateStrings.evaluateConnectionString(connectionName);
            }
            final String _connectionStringName = connectionStringName;

            thisResource.withAnnotation(new EnvironmentCallbackAnnotation("connectionStringConfig", context -> {
                context.getEnvironmentVariables().put(_connectionStringName,
                    new ConnectionStringReference<ResourceWithConnectionString<?>>(rwcs, optional));
            }));
        }

        if (resource instanceof ResourceWithEndpoints<?> otherResourceWithEnvironment) {
            ResourceUtilities.applyEndpoints(thisResourceWithEnvironment, otherResourceWithEnvironment, null);
        }

        return self();
    }
}

package com.microsoft.aspire.resources.traits;

import com.microsoft.aspire.implementation.ResourceUtilities;
import com.microsoft.aspire.implementation.TemplateStrings;
import com.microsoft.aspire.resources.Resource;
import com.microsoft.aspire.resources.references.ConnectionStringReference;
import com.microsoft.aspire.resources.annotations.EnvironmentCallbackAnnotation;

public interface ResourceWithReference<T extends Resource<T> & ResourceWithReference<T>> extends SelfAware<T> {

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
//
//        if (resource instanceof ResourceWithEndpoints<?> rwe) {
////            rwe.getEndpoints().forEach(endpoint -> {
////                String envVarName = "ENDPOINT";
////                String envVarValue = endpoint.getUrl();
////                withEnvironment(envVarName, envVarValue);
////            });
//        }
//
        if (resource instanceof ResourceWithConnectionString<?> rwcs) {
//            var resource = source.Resource;
//            connectionName ??= resource.Name;
//
//            return builder.WithEnvironment(context =>
//                {
//                    var connectionStringName = resource.ConnectionStringEnvironmentVariable ?? $"{ConnectionStringEnvironmentName}{connectionName}";
//
//            context.EnvironmentVariables[connectionStringName] = new ConnectionStringReference(resource, optional);
//        });
            boolean optional = false;
//            if (connectionName == null) {
                final String connectionName = resource.getName();
//            }

            String connectionStringName = rwcs.getConnectionStringEnvironmentVariable();
            if (connectionStringName == null) {
                connectionStringName = TemplateStrings.evaluateConnectionString(connectionName);
            }
            final String _connectionStringName = connectionStringName;

            thisResource.withAnnotation(new EnvironmentCallbackAnnotation("connectionStringConfig", context -> {
                context.getEnvironmentVariables().put(_connectionStringName, new ConnectionStringReference((Resource<?>) rwcs, optional));
            }));

//            // TODO anything to be done here?
//            String envVarName = "ConnectionStrings__" + resource.getName();
//            String envVarValue = "{" + resource.getName() + ".connectionString}";
//            resourceWithEnvironment.withEnvironment(envVarName, envVarValue);
        }
//
//        if (resource instanceof ResourceWithBindings<?> rwb) {
//            rwb.getBindings().forEach((scheme, binding) -> {
//                String envVarName = "services__" + resource.getName() + "__" + scheme + "__0";
//                String envVarValue = "{" + resource.getName() + ".bindings." + scheme + ".url}";
//                resourceWithEnvironment.withEnvironment(envVarName, envVarValue);
//            });
//        }
//
//        if (resource instanceof ResourceWithParameters<?> rwp) {
//            // TODO anything to be done here?
//        }
//
//        if (resource instanceof ResourceWithEnvironment<?> rwe) {
//            // TODO anything to be done here?
//        }

        if (resource instanceof ResourceWithEndpoints<?> otherResourceWithEnvironment) {
            ResourceUtilities.applyEndpoints(thisResourceWithEnvironment, otherResourceWithEnvironment, null);
        }

        return self();
    }
}

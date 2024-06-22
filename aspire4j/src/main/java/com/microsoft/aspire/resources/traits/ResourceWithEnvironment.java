package com.microsoft.aspire.resources.traits;

import com.microsoft.aspire.resources.*;

import java.util.Map;

public interface ResourceWithEnvironment<T extends ResourceWithEnvironment<T>> {

    T withEnvironment(String key, String value);

    default T withEnvironment(Map<String, String> environment) {
        for (Map.Entry<String, String> entry : environment.entrySet()) {
            withEnvironment(entry.getKey(), entry.getValue());
        }
        return (T) this;
    }

    default T withReference(Resource resource) {
        // https://learn.microsoft.com/en-us/dotnet/api/aspire.hosting.resourcebuilderextensions.withreference?view=dotnet-aspire-8.0.1#aspire-hosting-resourcebuilderextensions-withreference-1(aspire-hosting-applicationmodel-iresourcebuilder((-0))-aspire-hosting-applicationmodel-iresourcebuilder((aspire-hosting-applicationmodel-iresourcewithconnectionstring))-system-string-system-boolean)
//        final String connectionName = resource.getConnectionName();

        // switch depending on the type of the resource
        switch (resource) {
            case Executable r -> {
                // TODO
                String envVarName = "ENDPOINT";
                String envVarValue = "{" + resource.getName() + ".connectionString}";
                withEnvironment(envVarName, envVarValue);
                break;
            }
            case Project r -> {
//                String envVarName = "ConnectionStrings__" + resource.getName();
//                String envVarValue = "{" + resource.getName() + ".connectionString}";
                // TODO
                throw new RuntimeException("Not implemented yet");
            }
            case DockerFile r -> {
                r.getBindings().forEach((scheme, binding) -> {
                    String envVarName = "services__" + resource.getName() + "__" + scheme + "__0";
                    String envVarValue = "{" + resource.getName() + ".bindings." + scheme + ".url}";
                    withEnvironment(envVarName, envVarValue);
                });
                break;
            }
            case Value r -> {
                // TODO
                String envVarName = "ENDPOINT";
                String envVarValue = "{" + resource.getName() + ".connectionString}";
                withEnvironment(envVarName, envVarValue);
            }
            default -> throw new IllegalArgumentException("Unknown resource type: " + resource);
        }
//        final String envVarName = "ConnectionStrings__" +
//                (connectionName != null && !connectionName.isEmpty() ? connectionName : resource.getName());
        return (T) this;
    }
}

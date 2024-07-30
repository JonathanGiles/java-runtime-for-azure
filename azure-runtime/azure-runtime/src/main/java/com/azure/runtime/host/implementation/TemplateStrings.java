package com.azure.runtime.host.implementation;

import com.azure.runtime.host.resources.Resource;
import com.azure.runtime.host.utils.templates.TemplateEngine;

import java.util.Map;

public class TemplateStrings {
    private TemplateStrings() { }

    private static final String BINDING_STRING = "{${resource.name}.bindings.${endpointName}.${property}}";

    private static final String SERVICES_STRING = "services__${resource.name}__${endpointName}__0";

    public static final String CONNECTION_STRING = "ConnectionStrings__${resourceName}";

    public static String evaluateBinding(Resource<?> resource, String endpointName, String property) {
        return evaluate(BINDING_STRING, Map.of(
            "resource", resource,
            "endpointName", endpointName,
            "property", property));
    }

    public static String evaluateService(String resourceName) {
        return evaluate(SERVICES_STRING, Map.of(
            "resource.name", resourceName,
            "endpointName", "default"));
    }

    public static String evaluateService(Resource<?> resource) {
        return evaluate(SERVICES_STRING, Map.of(
            "resource", resource,
            "endpointName", "default"));
    }

    public static String evaluateService(Resource<?> resource, String endpointName) {
        return evaluate(SERVICES_STRING, Map.of(
            "resource", resource,
            "endpointName", endpointName));
    }

    public static String evaluateConnectionString(String resourceName) {
        return evaluate(CONNECTION_STRING, Map.of(
            "resourceName", resourceName));
    }

    public static String evaluate(String template, Map<String, Object> context) {
        return TemplateEngine.getTemplateEngine().process(template, context);
    }
}

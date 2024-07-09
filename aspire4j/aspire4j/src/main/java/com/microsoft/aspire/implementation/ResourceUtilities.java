package com.microsoft.aspire.implementation;

import com.microsoft.aspire.resources.Resource;
import com.microsoft.aspire.resources.properties.*;
import com.microsoft.aspire.resources.traits.ResourceAnnotation;
import com.microsoft.aspire.resources.traits.ResourceWithEndpoints;
import com.microsoft.aspire.resources.traits.ResourceWithEnvironment;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceUtilities {
    public static final String ENV_VAR_CONNECTION_STRING = "ConnectionStrings__";

    private ResourceUtilities() { }

    /****************************************************************************************
     *
     * General utility API
     *
     ***************************************************************************************/

    public static <T extends ResourceAnnotation> Stream<T> getAnnotationsOfTypeAsStream(Resource<?> resource, Class<T> annotationClass) {
        return resource.getAnnotations().stream()
            .filter(annotationClass::isInstance)
            .map(annotationClass::cast);
    }

    public static <T extends ResourceAnnotation> List<T> getAnnotationsOfType(Resource<?> resource, Class<T> annotationClass) {
        return getAnnotationsOfTypeAsStream(resource, annotationClass).collect(Collectors.toList());
    }

    public static <T extends Resource<?>> T applyAnnotation(T resource, ResourceAnnotation annotation) {
        resource.withAnnotation(annotation);
        return resource;
    }


    /****************************************************************************************
     *
     * Endpoints
     *
     ***************************************************************************************/

    public static Stream<EndpointAnnotation> getEndpointAnnotationsAsStream(Resource<?> resource) {
        return getAnnotationsOfTypeAsStream(resource, EndpointAnnotation.class);
    }

    public static List<EndpointAnnotation> getEndpointAnnotations(Resource<?> resource) {
        return getEndpointAnnotationsAsStream(resource).collect(Collectors.toList());
    }

//    public static void withHttpEndpoint(Resource<?> resource,
//            String name, Integer port, Integer targetPort, boolean isExternal, boolean isProxied, String env) {
//        withEndpoint(resource, Transport.TCP, Scheme.HTTP, name, port, targetPort, isExternal, isProxied, env);
//    }

    public static void withEndpoint(Resource<?> resource,
            Transport transport, Scheme scheme, String name, Integer port, Integer targetPort, boolean isExternal, boolean isProxied, String env) {

        List<ResourceAnnotation> annotations = resource.getAnnotations();

        boolean endpointExists = annotations.stream()
            .filter(EndpointAnnotation.class::isInstance)
            .map(EndpointAnnotation.class::cast)
            .anyMatch(sb -> sb.getName().equalsIgnoreCase(name));

        if (endpointExists) {
            // TODO better exception
            throw new RuntimeException(String.format("Endpoint with name '%s' already exists", name));
        }

        EndpointAnnotation annotation = new EndpointAnnotation(
            Protocol.TCP,
            scheme,
            transport,
            name,
            port,
            targetPort,
            isExternal,
            isProxied);

        // TODO this is the C# translation
        // Set the environment variable on the resource
        if (env != null && resource instanceof ResourceWithEndpoints resourceWithEndpoints && resource instanceof ResourceWithEnvironment<?>) {
            // TODO
//            annotation.setTargetPortEnvironmentVariable(env);

            EndpointReference<?> endpointReference = new EndpointReference((Resource<?>) resourceWithEndpoints, annotation);

            resource.withAnnotation(new EnvironmentCallbackAnnotation("targetPortConfig", context -> {
                // FIXME Shouldn't matter for now, as env is always null
//                context.getEnvironmentVariables().put(env, endpointReference.getProperty(EndpointProperty.TARGET_PORT));
            }));
        }

        resource.withAnnotation(annotation);
    }

    public static <R extends ResourceWithEnvironment<?>> void applyEndpoints(
        R thisResource, ResourceWithEndpoints<?> resourceWithEndpoints, String endpointName) {

        Resource<?> _thisResource = (Resource<?>) thisResource;


        // When adding an endpoint we get to see whether there is an EndpointReferenceAnnotation
        // on the resource, if there is then it means we have already been here before and we can just
        // skip this and note the endpoint that we want to apply to the environment in the future
        // in a single pass. There is one EndpointReferenceAnnotation per endpoint source.
        EndpointReferenceAnnotation<?> endpointReferenceAnnotation = _thisResource.getAnnotations().stream()
            .filter(annotation -> annotation instanceof EndpointReferenceAnnotation &&
                ((EndpointReferenceAnnotation<?>) annotation).getResource().equals(resourceWithEndpoints))
            .map(annotation -> (EndpointReferenceAnnotation<?>) annotation)
            .findFirst()
            .orElse(null);

        if (endpointReferenceAnnotation == null) {
            endpointReferenceAnnotation = new EndpointReferenceAnnotation<>((Resource<?>) resourceWithEndpoints);
            _thisResource.withAnnotation(endpointReferenceAnnotation);
            thisResource.withEnvironment(createEndpointReferenceEnvironmentPopulationCallback(endpointReferenceAnnotation));
        }

        // If endpointName is null, use all endpoints, otherwise add the specific endpoint name.
        if (endpointName == null) {
            endpointReferenceAnnotation.setUseAllEndpoints(true);
        } else {
            endpointReferenceAnnotation.getEndpointNames().add(endpointName);
        }
    }

    private static Consumer<EnvironmentCallbackContext> createEndpointReferenceEnvironmentPopulationCallback(EndpointReferenceAnnotation<?> annotation) {
        return context -> {
            final Resource<?> resource = annotation.getResource();

            if (!(resource instanceof ResourceWithEndpoints<?>)) {
                throw new RuntimeException("Resource does not have endpoints");
            }
            ResourceWithEndpoints<?> resourceWithEndpoints = (ResourceWithEndpoints<?>) resource;
            final String serviceName = resource.getName();

            resourceWithEndpoints.getEndpoints().forEach(endpointReference -> {
                final String endpointName = endpointReference.getEndpointName();
                if (!annotation.isUseAllEndpoints() && !annotation.getEndpointNames().contains(endpointName)) {
                    // Skip this endpoint since it's not in the list of endpoints we want to reference.
                    return;
                }

                // Add the endpoint, rewriting localhost to the container host if necessary.
                context.getEnvironmentVariables().put(String.format("services__%s__%s__0", serviceName, endpointName), endpointReference);
            });
        };
    }


    /****************************************************************************************
     *
     * Environment
     *
     ***************************************************************************************/

    public static void withEnvironment(Resource<?> resource, String key, String value) {
        resource.withAnnotation(new EnvironmentAnnotation(key, value));
    }
}
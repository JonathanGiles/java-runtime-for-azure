package com.azure.runtime.host.implementation.utils.json;

import com.azure.runtime.host.resources.DockerFile;
import com.azure.runtime.host.resources.Resource;
import com.azure.runtime.host.resources.annotations.*;
import com.azure.runtime.host.resources.properties.Scheme;
import com.azure.runtime.host.resources.references.ReferenceExpression;
import com.azure.runtime.host.resources.traits.ManifestExpressionProvider;
import com.azure.runtime.host.resources.traits.ResourceWithArguments;
import com.azure.runtime.host.resources.traits.ResourceWithConnectionString;
import com.azure.runtime.host.resources.traits.ValueWithReferences;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.azure.runtime.host.implementation.ResourceUtilities;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ResourceSerializer extends JsonSerializer<Resource<?>> {
    private final Map<String, Resource<?>> referencedResources = new ConcurrentHashMap<>();
    private final Set<Object> currentDependencySet = new HashSet<>();

    @Override
    public void serialize(Resource<?> resource, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        Class<?> actualClass = resource.getClass();
        JavaType javaType = provider.constructType(actualClass);
        BeanDescription beanDesc = provider.getConfig().introspect(javaType);
        JsonSerializer<Object> serializer = BeanSerializerFactory.instance.findBeanOrAddOnSerializer(provider, javaType, beanDesc, true);

        // Serialize the object using the serializer for the actual class
        serializer.unwrappingSerializer(null).serialize(resource, gen, provider);

        // TODO this whole serializer could be made much more useful and general purpose - but for now we only use it
        // in this one situation, so the effort hasn't been made to do this yet.
        // Add the custom fields
        writeConnectionString(resource, gen);
        writeEnvironmentVariables(resource, gen);
        writeBindings(resource, gen);

        if (resource instanceof DockerFile<?> dockerResource) {
            writeObjectField("buildArgs", collectKeyValueAnnotations(dockerResource, "buildArgs"), gen);
        }

        if (resource instanceof ResourceWithArguments<?>) {
            writeObjectField("args", collectValueAnnotations(resource, "args"), gen);
        }

        gen.writeEndObject();
    }

    private void writeConnectionString(Resource<?> resource, JsonGenerator gen) {
        if (resource instanceof ResourceWithConnectionString<?> resourceWithConnectionString) {
            ReferenceExpression connectionStringExpression = resourceWithConnectionString.getConnectionStringExpression();
            if (connectionStringExpression != null) {
                try {
                    gen.writeStringField("connectionString", connectionStringExpression.getValueExpression());
                } catch (IOException e) {
                    throw new RuntimeException("Error writing connection string", e);
                }
            }
        }
    }

    private void writeBindings(Resource<?> resource, JsonGenerator gen) throws IOException {
        // TODO allocate dynamic target port if one isn't specified, e.g.
        //  https://github.com/dotnet/aspire/blob/a6e341ebbf956bbcec0dda304109815fcbae70c9/src/Aspire.Hosting/Publishing/ManifestPublishingContext.cs#L271
        Map<Scheme, EndpointAnnotation> bindingsMap = ResourceUtilities.getEndpointAnnotationsAsStream(resource)
            .collect(Collectors.toMap(EndpointAnnotation::getUriScheme, e -> e, (a, b) -> a));

        if (!bindingsMap.isEmpty()) {
            gen.writeObjectField("bindings", bindingsMap);
        }
    }

    private void writeEnvironmentVariables(Resource<?> resource, JsonGenerator gen) throws IOException {
        final Map<String, Object> config = new TreeMap<>(String::compareTo);
        EnvironmentCallbackContext envContext = new EnvironmentCallbackContext(config);

        List<EnvironmentCallbackAnnotation> callbacks = ResourceUtilities.getAnnotationsOfType(resource, EnvironmentCallbackAnnotation.class);
        if (callbacks != null && !callbacks.isEmpty()) {
            gen.writeFieldName("env");
            gen.writeStartObject();

            for (EnvironmentCallbackAnnotation callback : callbacks) {
                callback.getCallback().accept(envContext);
            }

            for (Map.Entry<String, Object> entry : config.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                String valueString;

                if (value instanceof String) {
                    valueString = (String) value;
                } else if (value instanceof ManifestExpressionProvider) {
                    valueString = ((ManifestExpressionProvider) value).getValueExpression();
                } else {
                    throw new RuntimeException("The value of the environment variable '" + key + "' is not supported.");
                }

                gen.writeStringField(key, valueString);

                // TODO see here:
                // https://github.com/dotnet/aspire/blob/a6e341ebbf956bbcec0dda304109815fcbae70c9/src/Aspire.Hosting/Publishing/ManifestPublishingContext.cs#L513
                tryAddDependentResources(value);
            }

            gen.writeEndObject();
        }
    }

    private List<Object> collectValueAnnotations(Resource<?> resource, String type) {
        return resource.getAnnotations().stream()
            .filter(a -> a instanceof ArgsAnnotation argsAnnotation && type.equals(argsAnnotation.getType()))
            .map(a -> ((ArgsAnnotation) a).getArgs().stream())
            .collect(Collectors.toList());
    }

    private Map<String, Object> collectKeyValueAnnotations(Resource<?> resource, String type) {
        return resource.getAnnotations().stream()
            .filter(a -> a instanceof KeyValueAnnotation keyValueAnnotation && type.equals(keyValueAnnotation.getType()))
            .map(a -> (KeyValueAnnotation) a)
            .collect(Collectors.toMap(KeyValueAnnotation::getKey, KeyValueAnnotation::getValue));
    }

    private void writeObjectField(String name, Map<?,?> map, JsonGenerator gen) throws IOException {
        Objects.requireNonNull(map);
        if (!map.isEmpty()) {
            gen.writeObjectField(name, map);
        }
    }

    private void writeObjectField(String name, Collection<?> collection, JsonGenerator gen) throws IOException {
        Objects.requireNonNull(collection);
        if (!collection.isEmpty()) {
            gen.writeObjectField(name, collection);
        }
    }

    private void tryAddDependentResources(Object value) {
        if (value instanceof Resource<?> resource) {
            referencedResources.put(resource.getName(), resource);
        } else if (value instanceof ValueWithReferences objectWithReferences) {
            if (!currentDependencySet.add(value)) {
                return; // Already processing this value, prevent infinite recursion
            }
            for (Object dependency : objectWithReferences.getReferences()) {
                if (!currentDependencySet.contains(dependency)) {
                    tryAddDependentResources(dependency);
                }
            }
            currentDependencySet.remove(value);
        }
    }
}
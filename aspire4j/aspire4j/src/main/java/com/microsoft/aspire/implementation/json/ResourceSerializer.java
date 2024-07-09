package com.microsoft.aspire.implementation.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.microsoft.aspire.implementation.ResourceUtilities;
import com.microsoft.aspire.resources.Resource;
import com.microsoft.aspire.resources.properties.*;
import com.microsoft.aspire.resources.traits.ManifestExpressionProvider;
import com.microsoft.aspire.resources.traits.ResourceWithConnectionString;
import com.microsoft.aspire.resources.traits.ValueWithReferences;

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
        JsonSerializer<Object> serializer = BeanSerializerFactory.instance.findBeanSerializer(provider, javaType, beanDesc);

        // Serialize the object using the serializer for the actual class
        serializer.unwrappingSerializer(null).serialize(resource, gen, provider);

        // TODO this whole serializer could be made much more useful and general purpose - but for now we only use it
        // in this one situation, so the effort hasn't been made to do this yet.
        // Add the custom fields
        writeConnectionString(resource, gen);
        writeEnvironmentVariables(resource, gen);
        writeBindings(resource, gen);

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
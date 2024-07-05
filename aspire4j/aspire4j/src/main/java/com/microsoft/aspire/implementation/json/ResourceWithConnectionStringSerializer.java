package com.microsoft.aspire.implementation.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.microsoft.aspire.resources.traits.ResourceWithConnectionString;

import java.io.IOException;

public class ResourceWithConnectionStringSerializer extends JsonSerializer<ResourceWithConnectionString<?>> {
    @Override
    public void serialize(ResourceWithConnectionString<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        Class<?> actualClass = value.getClass();
        JavaType javaType = provider.constructType(actualClass);
        BeanDescription beanDesc = provider.getConfig().introspect(javaType);
        JsonSerializer<Object> serializer = BeanSerializerFactory.instance.findBeanSerializer(provider, javaType, beanDesc);

        // Serialize the object using the serializer for the actual class
        serializer.unwrappingSerializer(null).serialize(value, gen, provider);

        // TODO this whole serializer could be made much more useful and general purpose - but for now we only use it
        // in this one situation, so the effort hasn't been made to do this yet.
        // Add the custom field
        gen.writeObjectField(value.getConnectionStringEnvironmentVariable(), value.getValue());

        gen.writeEndObject();
    }
}
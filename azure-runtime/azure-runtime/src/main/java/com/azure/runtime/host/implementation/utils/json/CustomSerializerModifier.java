package com.azure.runtime.host.implementation.utils.json;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.azure.runtime.host.utils.json.CustomSerialize;

import java.lang.annotation.Annotation;

public class CustomSerializerModifier extends BeanSerializerModifier {

    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
        CustomSerialize annotation = lookupAnnotation(beanDesc.getBeanClass(), CustomSerialize.class);
        if (annotation != null) {
            try {
                return (JsonSerializer<?>) annotation.serializer().getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Could not instantiate custom serializer for " + beanDesc.getBeanClass().getName(), e);
            }
        }
        return serializer;
    }

    private <T extends Annotation> T lookupAnnotation(Class<?> clazz, Class<T> customSerializeClass) {
        // check directly on the given bean class
        if (clazz.isAnnotationPresent(customSerializeClass)) {
            return clazz.getAnnotation(customSerializeClass);
        }

        // Check interfaces for the annotation
        for (Class<?> iface : clazz.getInterfaces()) {
            if (iface.isAnnotationPresent(customSerializeClass)) {
                return iface.getAnnotation(customSerializeClass);
            }
        }

        // Check superclass recursively
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && !superclass.equals(Object.class)) {
            return lookupAnnotation(superclass, customSerializeClass);
        }

        return null;
    }
}
package com.azure.runtime.host.implementation.utils.json;

import com.azure.runtime.host.utils.json.RelativePath;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

public class RelativePathModule extends Module {

    @Override
    public String getModuleName() {
        return "RelativePathModule";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addSerializers(new SimpleSerializers() {
            @Override
            public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
                if (beanDesc.getClassAnnotations().has(RelativePath.class)) {
                    return new RelativePathSerializer();
                }
                return super.findSerializer(config, type, beanDesc);
            }
        });

        context.insertAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            @Override
            protected boolean _isIgnorable(Annotated a) {
                if (a.hasAnnotation(RelativePath.class)) {
                    return false; // Ensure fields with @RelativePath are not ignored
                }
                return super._isIgnorable(a);
            }
        });
    }
}
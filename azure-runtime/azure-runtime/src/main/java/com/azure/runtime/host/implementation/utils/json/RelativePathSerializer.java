package com.azure.runtime.host.implementation.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.azure.runtime.host.utils.FileUtilities;
import com.azure.runtime.host.utils.json.RelativePath;

import java.io.IOException;
import java.nio.file.Path;

/**
 * This class exists to transform the user-facing path inputs (which are always relative to the azd execution directory)
 * into paths that are relative to the output directory. This is necessary because the aspire-manifest.json file is
 * generated in the output directory, and the paths in the manifest file need to be relative to that.
 */
public class RelativePathSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private final Path basePath;
    private final boolean isRelativePath;

    public RelativePathSerializer() {
        this(null, false);
    }

    private RelativePathSerializer(Path basePath, boolean isRelativePath) {
        this.basePath = basePath;
        this.isRelativePath = isRelativePath;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        boolean isRelativePath = property.getMember().hasAnnotation(RelativePath.class);
        return new RelativePathSerializer(basePath, isRelativePath);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (isRelativePath) {
            // When the path is specified in the Java Runtime for Azure API, it is specified as a path relative to the
            // root directory. When the path is output to the aspire-manifest.json file, the path will transform based
            // on the relative location of the output directory to the root directory. This way, when azd picks up the
            // manifest file, the paths remain correct.
            gen.writeString(FileUtilities.convertRootRelativePathToOutputPath(value).toString());
        } else {
            gen.writeString(value);
        }
    }
}
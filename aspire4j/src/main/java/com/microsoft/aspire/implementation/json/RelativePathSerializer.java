package com.microsoft.aspire.implementation.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RelativePathSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private static final ThreadLocal<Path> outputPath = new ThreadLocal<>();
    private Path basePath;
    private boolean isRelativePath;

    public RelativePathSerializer() {
        this.basePath = Paths.get(System.getProperty("user.dir"));
        this.isRelativePath = false;
    }

    private RelativePathSerializer(Path basePath, boolean isRelativePath) {
        this.basePath = basePath;
        this.isRelativePath = isRelativePath;
    }

    public static void setOutputPath(Path path) {
        outputPath.set(path);
    }

    public static Path getOutputPath() {
        return outputPath.get();
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        boolean isRelativePath = property.getMember().hasAnnotation(RelativePath.class);
        return new RelativePathSerializer(basePath, isRelativePath);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (isRelativePath) {
            // When the path is specified in the aspire api, it is specified as a path relative to the root directory.
            // When the path is output to the aspire-manifest.json file, the path will transform based on the relative
            // location of the output directory to the root directory. This way, when azd picks up the manifest file,
            // the paths remain correct.
            Path filePath = Paths.get(value).toAbsolutePath();
            Path relativePath = getOutputPath().toAbsolutePath().relativize(filePath);
            gen.writeString(relativePath.toString());
        } else {
            gen.writeString(value);
        }
    }
}
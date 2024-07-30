package com.azure.runtime.host.utils.templates;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The template engine is responsible for processing templates, replacing placeholders with values from the context.
 * Templates can be processed from strings or from files.
 */
public interface TemplateEngine {

    /**
     * Returns an instance of the template engine.
     * @return An instance of the template engine.
     */
    static TemplateEngine getTemplateEngine() {
        return FreeMarkerTemplateProcessor.getTemplateEngine();
    }

    /**
     * Processes the template files specified in the template descriptor list, using the context provided.
     * @param cls                   The class to use as the base for reading the template files.
     * @param templateDescriptors   The list of template descriptors to process.
     * @param context               The context to use when processing the templates.
     * @return A list of template file outputs.
     */
    default List<TemplateFileOutput> process(final Class<?> cls,
                                             final List<TemplateDescriptor> templateDescriptors,
                                             final Map<String, Object> context) {
        return templateDescriptors.stream().map(templateDescriptor -> {
            // read the file from our local resources directory
            InputStream resourceAsStream = cls.getResourceAsStream(templateDescriptor.inputFilename());
            if (resourceAsStream == null) {
                throw new RuntimeException("Resource file not found: " + templateDescriptor.inputFilename());
            }
            final String outputFilename = process(templateDescriptor.outputFilename(), context);
            final String outputString = process(resourceAsStream, context);
            return new TemplateFileOutput(outputFilename, outputString);
        }).collect(Collectors.toList());
    }

    /**
     * Processes the template content, replacing placeholders with values from the context.
     * @param templateContent    The template content to process.
     * @param context            The context to use when processing the template.
     * @return The processed template content.
     */
    String process(String templateContent, Map<String, Object> context);

    /**
     * Processes the template file, replacing placeholders with values from the context.
     * @param templateStream    The template file to process.
     * @param context           The context to use when processing the template.
     * @return The processed template content.
     */
    default String process(InputStream templateStream, Map<String, Object> context) {
        try {
            String templateContent = new String(templateStream.readAllBytes());
            return process(templateContent, context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

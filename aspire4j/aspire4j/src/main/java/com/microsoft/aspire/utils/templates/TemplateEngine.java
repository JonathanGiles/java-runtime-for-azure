package com.microsoft.aspire.utils.templates;

import com.microsoft.aspire.resources.traits.ResourceWithTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface TemplateEngine {

    static TemplateEngine getTemplateEngine() {
        return FreeMarkerTemplateProcessor.getTemplateEngine();
    }

    static List<ResourceWithTemplate.TemplateFileOutput> process(
                                             final Class<?> cls,
                                             final List<ResourceWithTemplate.TemplateDescriptor> templateDescriptorList,
                                             final Map<String, Object> context) {
        return templateDescriptorList.stream().map(templateDescriptor -> {
            // read the file from our local resources directory
            InputStream resourceAsStream = cls.getResourceAsStream(templateDescriptor.getInputFilename());
            if (resourceAsStream == null) {
                throw new RuntimeException("Resource file not found: " + templateDescriptor.getInputFilename());
            }
            final TemplateEngine templateEngine = TemplateEngine.getTemplateEngine();
            final String outputFilename = templateEngine.processTemplate(templateDescriptor.getOutputFilename(), context);
            final String outputString = templateEngine.processTemplate(resourceAsStream, context);
            return new ResourceWithTemplate.TemplateFileOutput(outputFilename, outputString);
        }).collect(Collectors.toList());
    }

    String processTemplate(String templateContent, Map<String, Object> context);

    default String processTemplate(InputStream templateStream, Map<String, Object> context) {
        try {
            String templateContent = new String(templateStream.readAllBytes());
            return processTemplate(templateContent, context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

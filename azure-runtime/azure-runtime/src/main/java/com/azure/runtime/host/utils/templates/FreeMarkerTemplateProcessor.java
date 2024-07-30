package com.azure.runtime.host.utils.templates;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

// Non-public API
class FreeMarkerTemplateProcessor implements TemplateEngine {
    private static final TemplateEngine INSTANCE = new FreeMarkerTemplateProcessor();

    private final Configuration CONFIG = new Configuration(Configuration.VERSION_2_3_33);

    private FreeMarkerTemplateProcessor() {    }

    public static TemplateEngine getTemplateEngine() {
        return INSTANCE;
    }

    @Override
    public String process(String templateContent, Map<String, Object> context) {
        StringWriter out = new StringWriter();

        try {
            Template template = new Template("template", new StringReader(templateContent), CONFIG);
            template.process(context, out);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }

        return out.toString();
    }
}
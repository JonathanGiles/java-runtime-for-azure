package com.microsoft.aspire.utils.templates;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

public class FreeMarkerTemplateProcessor implements TemplateEngine {

    @Override
    public String processTemplate(String templateContent, Map<String, Object> context) {
        Configuration cfg = new Configuration(new Version("2.3.31"));
        StringWriter out = new StringWriter();

        try {
            Template template = new Template("template", new StringReader(templateContent), cfg);
            template.process(context, out);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }

        return out.toString();
    }
}
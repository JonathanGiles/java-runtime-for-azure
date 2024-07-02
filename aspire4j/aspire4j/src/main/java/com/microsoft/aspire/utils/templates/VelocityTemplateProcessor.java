//package com.microsoft.aspire.utils.templates;
//
//import org.apache.velocity.app.Velocity;
//import org.apache.velocity.app.VelocityEngine;
//import org.apache.velocity.Template;
//import org.apache.velocity.VelocityContext;
//
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.io.StringWriter;
//import java.util.Properties;
//
//public class VelocityTemplateProcessor {
//    private static final VelocityEngine velocityEngine;
//    static {
//        velocityEngine = new VelocityEngine();
//        Properties properties = new Properties();
//        properties.setProperty("resource.loader", "class");
//        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//        velocityEngine.init(properties);
//    }
//
//    private VelocityTemplateProcessor() {    }
//
//    public static String processTemplate(String templateName, VelocityContext context) {
//        Template template = velocityEngine.getTemplate(templateName);
//
//        // Merge the context with the template
//        StringWriter writer = new StringWriter();
//        template.merge(context, writer);
//
//        return writer.toString();
//    }
//
//    public static String processTemplateFromString(String templateContent, VelocityContext context) {
//        StringWriter writer = new StringWriter();
//        velocityEngine.evaluate(context, writer, "TemplateProcessor", templateContent);
//        return writer.toString();
//    }
//
//    public static String processTemplateFromStream(InputStream templateStream, VelocityContext context) {
//        Reader reader = new InputStreamReader(templateStream);
//        StringWriter writer = new StringWriter();
//        velocityEngine.evaluate(context, writer, "TemplateProcessor", reader);
//        return writer.toString();
//    }
//}
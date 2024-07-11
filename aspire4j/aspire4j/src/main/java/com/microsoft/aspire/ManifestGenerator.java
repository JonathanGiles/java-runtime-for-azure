package com.microsoft.aspire;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.*;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.microsoft.aspire.utils.json.CustomSerializerModifier;
import com.microsoft.aspire.resources.traits.ResourceWithLifecycle;
import com.microsoft.aspire.resources.traits.ResourceWithTemplate;
import com.microsoft.aspire.utils.FileUtilities;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

// Not public API
class ManifestGenerator {

    private static final Logger LOGGER = Logger.getLogger(ManifestGenerator.class.getName());

    private Path outputPath;

    void generateManifest(AppHost appHost, Path outputPath) {
        this.outputPath = outputPath;

        File outputDir = outputPath.toFile();
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        FileUtilities.setOutputPath(outputPath);

        DistributedApplication app = new DistributedApplication();
        appHost.configureApplication(app);
        processTemplates(app, outputPath);
        writeManifest(app);
    }

    private void processTemplates(DistributedApplication app, Path outputPath) {
        LOGGER.info("Processing templates...");
        app.manifest.getResources().values().stream()
            .filter(r -> r instanceof ResourceWithTemplate<?>)
            .map(r -> (ResourceWithTemplate<?>) r)
            .map(ResourceWithTemplate::processTemplate)
            .forEach(templateFiles -> templateFiles.forEach(this::writeTemplateFile));
        LOGGER.info("Templates processed");
    }

    private void writeManifest(DistributedApplication app) {
        if (app.manifest.isEmpty()) {
            LOGGER.info("No configuration received from AppHost...exiting");
            System.exit(-1);
        }

        // run the precommit lifecycle hook on all resources
        app.manifest.getResources().values().iterator().forEachRemaining(ResourceWithLifecycle::onResourcePrecommit);

        LOGGER.info("Validating models...");
        // Firstly, disable the info logging messages that are printed by Hibernate Validator
        Logger.getLogger("org.hibernate.validator.internal.util.Version").setLevel(Level.OFF);

        // Get the logger for the Hibernate Validator class
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<AspireManifest>> violations = validator.validate(app.manifest);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<AspireManifest> violation : violations) {
                LOGGER.warning(violation.getMessage());
            }
            LOGGER.warning("Failed...exiting");
            System.exit(-1);
        } else {
            // object is valid, continue processing...
            LOGGER.info("Models validated...Writing manifest to file");
        }

        // Jackson ObjectMapper is used to serialize the AspireManifest object to a JSON string,
        // and write to a file named "aspire-manifest.json".
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.setSerializerModifier(new CustomSerializerModifier());
        objectMapper.registerModule(module);

        printAnnotations(System.out, app);

        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(outputPath.toFile(), "aspire-manifest.json"), app.manifest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("Manifest written to file");
    }

    private void writeTemplateFile(ResourceWithTemplate.TemplateFileOutput templateFile) {
        try {
            Path path = Paths.get(outputPath.toString() + "/" + templateFile.filename());

            // ensure the parent directories exist
            Files.createDirectories(path.getParent());
            Files.write(path, templateFile.content().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printAnnotations(PrintStream out, DistributedApplication app) {
        app.manifest.getResources().values().forEach(resource -> {
            out.println("Resource: " + resource.getName());
            resource.getAnnotations().forEach(annotation -> {
                out.println("  Annotation: " + annotation);
            });
        });
    }
}

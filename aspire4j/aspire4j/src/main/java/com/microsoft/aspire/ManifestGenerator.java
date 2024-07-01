package com.microsoft.aspire;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.microsoft.aspire.implementation.json.RelativePathSerializer;
import com.microsoft.aspire.resources.AzureBicepResource;
import com.microsoft.aspire.resources.Resource;
import com.microsoft.aspire.resources.traits.ResourceWithTemplate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

// Not public API
class ManifestGenerator {

    private static final Logger LOGGER = Logger.getLogger(ManifestGenerator.class.getName());

    private Path outputPath;

    void generateManifest(AppHost appHost, Path outputPath) {
        this.outputPath = outputPath;

        DistributedApplication app = new DistributedApplication();
        appHost.configureApplication(app);
        app.performResourceIntrospection();
        processTemplates(app);
        writeManifest(app);
    }

    private void processTemplates(DistributedApplication app) {
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

        File outputDir = outputPath.toFile();
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // Set the outputDir in the RelativePathSerializer
        RelativePathSerializer.setOutputPath(outputPath);

        // Jackson ObjectMapper is used to serialize the AspireManifest object to a JSON string,
        // and write to a file named "aspire-manifest.json".
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(outputDir, "aspire-manifest.json"), app.manifest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("Manifest written to file");
    }

    private void writeTemplateFile(ResourceWithTemplate.TemplateFileOutput templateFile) {
        try {
            Files.write(Paths.get(outputPath.toString() + "/" + templateFile.filename()), templateFile.content().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void writeBicep(DistributedApplication app) {
//        LOGGER.info("Writing Bicep files...");
//
//        // iterate through the resources in the app, and for any that are of type AzureBicep, give them the opportunity
//        // to write their bicep file to the output directory.
//        for (Resource resource : app.manifest.getResources().values()) {
//            if (resource instanceof AzureBicepResource azureBicepResource) {
//                List<ResourceWithTemplate.TemplateFileOutput> bicepFiles = azureBicepResource.getBicepFiles();
//
//                for (ResourceWithTemplate.TemplateFileOutput bicepFile : bicepFiles) {
//                    try {
//                        Files.write(Paths.get(outputPath.toString() + "/" + bicepFile.filename()), bicepFile.content().getBytes());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
}

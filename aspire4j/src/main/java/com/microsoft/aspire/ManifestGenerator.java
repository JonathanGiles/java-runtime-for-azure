package com.microsoft.aspire;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

// Not public API
class ManifestGenerator {
    private static final String OUTPUT_DIR = "output";

    private static final Logger LOGGER = Logger.getLogger(ManifestGenerator.class.getName());

    void run(AppHost appHost) {
        DistributedApplication app = new DistributedApplication();
        appHost.configureApplication(app);
        writeManifest(app);
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

        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

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

        writeBicep();
    }

    private void writeBicep() {
        LOGGER.info("Writing Bicep files...");
        try {
            // Get the URL of the 'storage.module.bicep' resource
            URL resourceUrl = ManifestGenerator.class.getResource("storage.module.bicep");
            if (resourceUrl == null) {
                LOGGER.warning("Resource not found: storage.module.bicep");
                System.exit(-1);
            }

            // Convert the URL to a URI
            URI resourceUri = resourceUrl.toURI();

            // Check if the URI is a jar URI
            if (resourceUri.getScheme().equals("jar")) {
                InputStream resourceAsStream = ManifestGenerator.class.getResourceAsStream("storage.module.bicep");
                writeToFile(resourceAsStream, Paths.get(OUTPUT_DIR  + "/storage.module.bicep"));
            } else {
                // The URI is not a jar URI, so just copy the file
                Files.copy(Paths.get(resourceUri),
                        Paths.get(OUTPUT_DIR  + "/storage.module.bicep"),
                        StandardCopyOption.REPLACE_EXISTING);
            }

            LOGGER.info("Done");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(InputStream resourceAsStream, Path path) {
        try {
            LOGGER.info("Writing bicep file to " + path);
            Files.copy(resourceAsStream, path, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("Done writing bicep file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

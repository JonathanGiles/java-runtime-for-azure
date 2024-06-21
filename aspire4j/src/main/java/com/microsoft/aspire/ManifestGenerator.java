package com.microsoft.aspire;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import com.microsoft.aspire.implementation.manifest.AspireManifest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class ManifestGenerator {
    private static final String OUTPUT_DIR = "output";

    public static void main(String[] args) {
        System.out.println("Starting Manifest Generator...");
        ManifestGenerator manifestGenerator = new ManifestGenerator();
        manifestGenerator.run();
    }

    private void run() {
        System.out.println("Looking for AppHost implementations...");

        int count = 0;
        ServiceLoader<AppHost> serviceLoader = ServiceLoader.load(AppHost.class);
        for (AppHost appHost : serviceLoader) {
            count++;
            System.out.println("Found " + appHost.getClass());
            DistributedApplication app = new DistributedApplication();

            System.out.print("Receiving configuration from AppHost...");
            appHost.configureApplication(app);
            System.out.println("Done");

            writeManifest(app);
        }

        if (count == 0) {
            System.out.println("No AppHost implementations found...exiting");
            System.exit(-1);
        }
    }

    public void run(AppHost appHost) {
        DistributedApplication app = new DistributedApplication();
        appHost.configureApplication(app);
        writeManifest(app);
    }

    private void writeManifest(DistributedApplication app) {
        System.out.println("Validating models...");
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<AspireManifest>> violations = validator.validate(app.manifest);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<AspireManifest> violation : violations) {
                System.err.println(violation.getMessage());
            }
            System.out.println("Failed...exiting");
            System.exit(-1);
        } else {
            // object is valid, continue processing...
            System.out.println("Models validated...Writing manifest to file");
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
        System.out.println("Manifest written to file");

        writeBicep();
    }

    private void writeBicep() {
        System.out.print("Writing Bicep files...");
        try {
            // Get the URL of the 'storage.module.bicep' resource
            URL resourceUrl = ManifestGenerator.class.getResource("storage.module.bicep");
            if (resourceUrl == null) {
                System.err.println("Resource not found: storage.module.bicep");
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

            System.out.println("Done");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(InputStream resourceAsStream, Path path) {
        try {
            System.out.println("Writing bicep file to " + path);
            Files.copy(resourceAsStream, path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Done writing bicep file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.microsoft.aspire;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;

import com.microsoft.aspire.implementation.manifest.AspireManifest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class ManifestGenerator {
    private static final String OUTPUT_DIR = "output";

//    public static void main(String[] args) {
//        ManifestGenerator manifestGenerator = new ManifestGenerator();
//        manifestGenerator.run();
//    }
//
//    private void run() {
//        ServiceLoader<AppHost> serviceLoader = ServiceLoader.load(AppHost.class);
//        for (AppHost appHost : serviceLoader) {
//            DistributedApplication app = new DistributedApplication();
//            appHost.configureApplication(app);
//            writeManifest(app);
//        }
//    }

    public void run(AppHost appHost) {
        DistributedApplication app = new DistributedApplication();
        appHost.configureApplication(app);
        writeManifest(app);
    }

    private void writeManifest(DistributedApplication app) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<AspireManifest>> violations = validator.validate(app.manifest);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<AspireManifest> violation : violations) {
                System.out.println(violation.getMessage());
            }
        } else {
            // object is valid, continue processing...
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

        writeBicep();
    }

    private void writeBicep() {
        // FIXME
        // For now, copy the resources/storage.module.bicep file to the output directory
        try {
            // Get the URL of the 'storage.module.bicep' resource
            URL resourceUrl = ManifestGenerator.class.getResource("/storage.module.bicep");
            if (resourceUrl == null) {
                System.out.println("Resource not found: storage.module.bicep");
                return;
            }

            // Convert the URL to a URI, then to a Path
            Path sourcePath = Paths.get(resourceUrl.toURI());

            // Copy the file
            Files.copy(sourcePath,
                    Paths.get(OUTPUT_DIR  + "/storage.module.bicep"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

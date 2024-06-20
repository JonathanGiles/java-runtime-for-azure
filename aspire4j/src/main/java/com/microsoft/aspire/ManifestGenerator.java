package com.microsoft.aspire;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ServiceLoader;

public class ManifestGenerator {

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
        // Jackson ObjectMapper is used to serialize the AspireManifest object to a JSON string,
        // and write to a file named "aspire-manifest.json".
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File("aspire-manifest.json"), app.manifest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

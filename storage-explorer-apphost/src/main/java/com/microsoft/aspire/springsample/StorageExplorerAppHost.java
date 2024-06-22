package com.microsoft.aspire.springsample;

import com.microsoft.aspire.AppHost;
import com.microsoft.aspire.DistributedApplication;

public class StorageExplorerAppHost implements AppHost {

    public static void main(String[] args) {
        new StorageExplorerAppHost().run();
    }

    @Override
    public void configureApplication(DistributedApplication app) {
        var azureStorage = app.addAzureStorage("storage");
        var blobStorage = azureStorage.addBlobs("storage-explorer-blobs");

        var dateService = app.addDockerFile("dateservice", "../date-service/Dockerfile", "../date-service")
            .withExternalHttpEndpoints();

        var storageExplorer = app.addDockerFile("storage-explorer", "../storage-explorer/Dockerfile", "../storage-explorer")
            .withExternalHttpEndpoints()
            .withReference(blobStorage)
            .withReference(dateService);
    }
}

package com.microsoft.aspire.springsample;

import com.microsoft.aspire.AppHost;
import com.microsoft.aspire.DistributedApplication;

// Inspired by:
// https://github.com/dotnet/aspire/blob/main/playground/AzureStorageEndToEnd/AzureStorageEndToEnd.AppHost/Program.cs
public class StorageExplorerAppHost implements AppHost {

    public static void main(String[] args) {
        new StorageExplorerAppHost().run();
    }

    @Override
    public void configureApplication(DistributedApplication app) {
        var azureStorage = app.addAzureStorage("storage");
        var blobStorage = azureStorage.addBlobs("blobs");

//        app.addProject("spring-sample")
//            .withExternalHttpEndpoints()
//            .withReference(blobStorage);

        app.addContainer("spring-storage-explorer", "image-file-TODO")
            .withExternalHttpEndpoints()
            .withReference(blobStorage);

        // Wire up the aspire-dashboard container
        app.addAspireDashboard();
    }
}

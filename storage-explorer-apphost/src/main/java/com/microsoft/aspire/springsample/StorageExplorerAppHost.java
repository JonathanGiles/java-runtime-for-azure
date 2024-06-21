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
        var blobStorage = azureStorage.addBlobs("storage-explorer-blobs");

        // If we could just point to a project (in the .net sense), we could do the following:
//        app.addProject("spring-sample")
//            .withExternalHttpEndpoints()
//            .withReference(blobStorage);

        app.addDockerFile("storage-explorer", "../storage-explorer/Dockerfile", "../storage-explorer")
            .withExternalHttpEndpoints()
            .withReference(blobStorage);
    }
}

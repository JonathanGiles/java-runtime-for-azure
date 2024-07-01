package com.microsoft.aspire.storageexplorer;

import com.microsoft.aspire.AppHost;
import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.extensions.azure.storage.AzureStorageExtension;
import com.microsoft.aspire.extensions.spring.SpringExtension;

public class StorageExplorerAppHost implements AppHost {

    @Override public void configureApplication(DistributedApplication app) {
        app.printExtensions();

        var azureStorage = app.withExtension(AzureStorageExtension.class)
                .addAzureStorage("storage");

        var blobStorage = azureStorage.addBlobs("storage-explorer-blobs");

        var dateService = app.withExtension(SpringExtension.class)
            .addSpringProject("date-service-spring")
            .withPath("date-service")
            .withExternalHttpEndpoints();

        var storageExplorer = app.withExtension(SpringExtension.class)
            .addSpringProject("storage-explorer-spring")
            .withPath("storage-explorer")
            .withExternalHttpEndpoints()
            .withReference(blobStorage)
            .withReference(dateService);

        // Old style, with direct reference to dockerfiles
//        var dateService = app.addDockerFile("dateservice", "date-service/Dockerfile", "date-service")
//            .withExternalHttpEndpoints();
//
//        var storageExplorer = app.addDockerFile("storage-explorer", "storage-explorer/Dockerfile", "storage-explorer")
//            .withExternalHttpEndpoints()
//            .withReference(blobStorage)
//            .withReference(dateService);
    }

    public static void main(String[] args) {
        new StorageExplorerAppHost().boot(args);
    }
}

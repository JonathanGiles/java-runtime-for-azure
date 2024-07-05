package com.microsoft.aspire.storageexplorer;

import com.microsoft.aspire.AppHost;
import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.extensions.azure.openai.AzureOpenAIExtension;
import com.microsoft.aspire.extensions.azure.openai.resources.AzureOpenAIDeployment;
import com.microsoft.aspire.extensions.azure.storage.AzureStorageExtension;
import com.microsoft.aspire.extensions.spring.SpringExtension;

import static com.microsoft.aspire.extensions.azure.openai.resources.AzureOpenAIDeployment.using;

public class StorageExplorerAppHost implements AppHost {

    @Override public void configureApplication(DistributedApplication app) {
        app.printExtensions();

        // Create Azure Storage resources...
        var azureStorage = app.withExtension(AzureStorageExtension.class)
            .addAzureStorage("storage");

        var blobStorage = azureStorage.addBlobs("storage-explorer-blobs");

        // Create Azure OpenAI resources...
         var openAI = app.withExtension(AzureOpenAIExtension.class)
             .addAzureOpenAI("openai")
             .withDeployment(using("gpt-35-turbo", "gpt-35-turbo", "0613"));

        var eurekaServiceDiscovery = app.withExtension(SpringExtension.class)
            .addEurekaServiceDiscovery("eureka");

        var dateService = app.withExtension(SpringExtension.class)
            .addSpringProject("date-service-spring")
            .withPath("date-service")
            .withExternalHttpEndpoints();

        var storageExplorer = app.withExtension(SpringExtension.class)
            .addSpringProject("storage-explorer-spring")
            .withPath("storage-explorer")
            .withExternalHttpEndpoints()
            .withReference(blobStorage)
            .withReference(dateService)
            .withReference(eurekaServiceDiscovery)
            .withReference(openAI);

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

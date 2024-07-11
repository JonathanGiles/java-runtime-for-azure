package com.microsoft.aspire.storageexplorer;

import com.microsoft.aspire.AppHost;
import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.extensions.azure.storage.AzureStorageExtension;
import com.microsoft.aspire.extensions.spring.SpringExtension;

public class StorageExplorerAppHost implements AppHost {

    @Override public void configureApplication(DistributedApplication app) {
        app.printExtensions();

        // Create Azure Storage resources...
        var blobStorage = app.withExtension(AzureStorageExtension.class)
            .addAzureStorage("storage")
            .addBlobs("storage-explorer-blobs");

        // Create Azure OpenAI resources...
//         var openAI = app.withExtension(AzureOpenAIExtension.class)
//             .addAzureOpenAI("openai")
//             .withDeployment(using("gpt-35-turbo", "gpt-35-turbo", "0613"));

        // Let's bring Spring in to the mix...
        var spring = app.withExtension(SpringExtension.class);

        // Sprinkle in some Spring Eureka service discovery, so our microservices don't need to know about each other
        var eurekaServiceDiscovery = spring.addEurekaServiceDiscovery("eureka");

        // add our first Spring Boot project - a date service that tells us the current date / time
        var dateService = spring.addSpringProject("date-service")
            .withReference(eurekaServiceDiscovery)
            .withExternalHttpEndpoints();

        // and storage explorer - a webapp to upload / download / view resources in a storage blob container
        var storageExplorer = spring.addSpringProject("storage-explorer")
            .withExternalHttpEndpoints()
            .withReference(blobStorage)
            .withReference(eurekaServiceDiscovery)
            .withOpenTelemetry();
//            .withReference(openAI);
    }

    public static void main(String[] args) {
        new StorageExplorerAppHost().boot(args);
    }
}

package ${package};

import com.azure.runtime.host.DistributedApplication;

#if ($includeAzure == "true")
import com.azure.runtime.host.extensions.azure.storage.AzureStorageExtension;
#end
#if ($includeSpring== "true")
import com.azure.runtime.host.extensions.spring.SpringExtension;
#end

public class AppHost implements com.azure.runtime.host.AppHost {

    @Override public void configureApplication(DistributedApplication app) {
        app.printExtensions();

#if ($includeAzure == "true")
        var azureStorage = app.withExtension(AzureStorageExtension.class)
                .addAzureStorage("storage");

        var blobStorage = azureStorage.addBlobs("storage-explorer-blobs");
#end

#if ($includeSpring == "true")
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
#end
    }

    public static void main(String[] args) {
        new AppHost().boot(args);
    }
}

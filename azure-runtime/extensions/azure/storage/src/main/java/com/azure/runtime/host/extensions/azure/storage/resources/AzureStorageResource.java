package com.azure.runtime.host.extensions.azure.storage.resources;

import com.azure.runtime.host.DistributedApplication;
import com.azure.runtime.host.resources.AzureBicepResource;
import com.azure.runtime.host.resources.ResourceType;
import com.azure.runtime.host.resources.traits.ResourceWithEndpoints;
import com.azure.runtime.host.utils.templates.TemplateDescriptor;
import com.azure.runtime.host.utils.templates.TemplateDescriptorsBuilder;
import com.azure.runtime.host.utils.templates.TemplateEngine;
import com.azure.runtime.host.utils.templates.TemplateFileOutput;

import java.util.List;
import java.util.Map;

public class AzureStorageResource extends AzureBicepResource<AzureStorageResource>
                                  implements ResourceWithEndpoints<AzureStorageResource> {
    private static final ResourceType AZURE_STORAGE = ResourceType.fromString("azure.bicep.v0");

//    internal ReferenceExpression GetTableConnectionString() => IsEmulator
//        ? ReferenceExpression.Create($"{AzureStorageEmulatorConnectionString.Create(tablePort: EmulatorTableEndpoint.Port)}")
//        : ReferenceExpression.Create($"{TableEndpoint}");
//
//    internal ReferenceExpression GetQueueConnectionString() => IsEmulator
//        ? ReferenceExpression.Create($"{AzureStorageEmulatorConnectionString.Create(queuePort: EmulatorQueueEndpoint.Port)}")
//        : ReferenceExpression.Create($"{QueueEndpoint}");
//
//    internal ReferenceExpression GetBlobConnectionString() => IsEmulator
//        ? ReferenceExpression.Create($"{AzureStorageEmulatorConnectionString.Create(blobPort: EmulatorBlobEndpoint.Port)}")
//        : ReferenceExpression.Create($"{BlobEndpoint}");

//    static final ReferenceExpression TABLE_CONNECTION_STRING = ReferenceExpression.create("tableEndpoint");
//    static final ReferenceExpression QUEUE_CONNECTION_STRING = ReferenceExpression.create("queueEndpoint");
//    static final ReferenceExpression BLOB_CONNECTION_STRING = ReferenceExpression.create("blobEndpoint");

    public AzureStorageResource(String name) {
        super(AZURE_STORAGE, name);
        withParameter("principalId", "");
        withParameter("principalType", "");
    }

    public AzureStorageBlobsResource addBlobs(String name) {
        return DistributedApplication.getInstance().addResource(new AzureStorageBlobsResource(name, this));
    }

    public AzureStorageQueuesResource addQueues(String name) {
        return DistributedApplication.getInstance().addResource(new AzureStorageQueuesResource(name, this));
    }

    public AzureStorageTablesResource addTables(String name) {
        return DistributedApplication.getInstance().addResource(new AzureStorageTablesResource(name, this));
    }

    @Override
    public List<TemplateFileOutput> processTemplate() {
        final String templatePath = "/templates/bicep/";
        List<TemplateDescriptor> templateFiles = TemplateDescriptorsBuilder.begin(templatePath)
            .with("storage.module.bicep", "${name}.module.bicep")
            .build();

        List<TemplateFileOutput> templateOutput = TemplateEngine.getTemplateEngine()
            .process(AzureStorageResource.class, templateFiles, Map.of("name", getName()));

        // we know that we need to get the output filename from the first element, and set that as the path
        // FIXME we need a better way of determining the output path of the template
        withPath(templateOutput.get(0).filename());

        return templateOutput;
    }
}

package com.microsoft.aspire.extensions.azure.storage.resources;

public final class AzureStorageQueuesResource extends AzureStorageChildResource {

    public AzureStorageQueuesResource(String name, AzureStorageResource storageResource) {
        super(name, storageResource, "queueEndpoint");
    }
}

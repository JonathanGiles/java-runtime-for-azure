package com.microsoft.aspire.extensions.azure.storage.resources;

public final class AzureStorageBlobsResource extends AzureStorageChildResource {

    public AzureStorageBlobsResource(String name, AzureStorageResource storageResource) {
        super(name, storageResource, "blobEndpoint");
    }
}

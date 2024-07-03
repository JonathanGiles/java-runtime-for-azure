package com.microsoft.aspire.extensions.azure.storage.resources;

public final class AzureStorageTablesResource extends AzureStorageChildResource {

    public AzureStorageTablesResource(String name, AzureStorageResource storageResource) {
        super(name, storageResource, "tableEndpoint");
    }
}

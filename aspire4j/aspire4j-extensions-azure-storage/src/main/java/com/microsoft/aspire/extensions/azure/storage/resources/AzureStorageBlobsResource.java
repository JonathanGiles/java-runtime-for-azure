package com.microsoft.aspire.extensions.azure.storage.resources;

import com.microsoft.aspire.resources.Value;
import com.microsoft.aspire.resources.traits.ResourceWithConnectionString;

public final class AzureStorageBlobsResource extends Value
        implements ResourceWithConnectionString<AzureStorageBlobsResource> {
    private final AzureStorageResource storageResource;
    private final String connectionString;

    public AzureStorageBlobsResource(String name, AzureStorageResource storageResource) {
        super(name);
        this.storageResource = storageResource;
        this.connectionString = storageResource.getName() + ".outputs.blobEndpoint";
        withProperty("connectionString", connectionString);
    }

//    @Override
//    @JsonIgnore
//    public String getConnectionString() {
//        return connectionString;
//    }
}

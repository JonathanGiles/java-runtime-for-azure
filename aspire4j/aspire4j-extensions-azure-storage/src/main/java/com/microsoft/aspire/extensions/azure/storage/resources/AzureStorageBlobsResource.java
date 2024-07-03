package com.microsoft.aspire.extensions.azure.storage.resources;

import com.microsoft.aspire.resources.Value;
import com.microsoft.aspire.resources.traits.ResourceWithConnectionString;
import com.microsoft.aspire.resources.traits.ResourceWithParent;

public final class AzureStorageBlobsResource extends Value<AzureStorageBlobsResource>
                                             implements ResourceWithConnectionString<AzureStorageBlobsResource>,
                                                        ResourceWithParent<AzureStorageResource> {
    private final AzureStorageResource storageResource;

    public AzureStorageBlobsResource(String name, AzureStorageResource storageResource) {
        super(name);
        this.storageResource = storageResource;
        withProperty(getConnectionStringEnvironmentVariable(), getValue());
    }

    @Override
    public AzureStorageResource getParent() {
        return storageResource;
    }

    @Override
    public String getConnectionStringEnvironmentVariable() {
        return "connectionString";
    }

    @Override
    public String getValue() {
        return storageResource.getName() + ".outputs.blobEndpoint";
    }
}

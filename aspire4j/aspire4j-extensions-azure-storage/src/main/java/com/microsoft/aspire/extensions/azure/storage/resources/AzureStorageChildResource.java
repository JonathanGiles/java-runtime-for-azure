package com.microsoft.aspire.extensions.azure.storage.resources;

import com.microsoft.aspire.resources.Value;
import com.microsoft.aspire.resources.traits.ResourceWithConnectionString;
import com.microsoft.aspire.resources.traits.ResourceWithParent;

public class AzureStorageChildResource extends Value<AzureStorageChildResource>
                                             implements ResourceWithConnectionString<AzureStorageChildResource>,
                                                        ResourceWithParent<AzureStorageResource> {
    private final AzureStorageResource storageResource;
    private final String endpointSuffix;

    public AzureStorageChildResource(String name, AzureStorageResource storageResource, String endpointSuffix) {
        super(name);
        this.storageResource = storageResource;
        this.endpointSuffix = endpointSuffix;
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
        return storageResource.getName() + ".outputs." + endpointSuffix;
    }
}

package com.microsoft.aspire.extensions.azure.storage.resources;

import com.microsoft.aspire.resources.Resource;
import com.microsoft.aspire.resources.ResourceType;
import com.microsoft.aspire.resources.traits.ResourceWithConnectionString;
import com.microsoft.aspire.resources.traits.ResourceWithParent;

public class AzureStorageChildResource extends Resource<AzureStorageChildResource>
                                       implements ResourceWithConnectionString<AzureStorageChildResource>,
                                                  ResourceWithParent<AzureStorageResource> {
    private final AzureStorageResource storageResource;
    private final String endpointSuffix;

    public AzureStorageChildResource(String name, AzureStorageResource storageResource, String endpointSuffix) {
        super(ResourceType.VALUE, name);
        this.storageResource = storageResource;
        this.endpointSuffix = endpointSuffix;
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
        // FIXME this kind of concatenation is error prone
        return "{" + storageResource.getName() + ".outputs." + endpointSuffix + "}";
    }

    @Override
    public AzureStorageChildResource self() {
        return this;
    }
}

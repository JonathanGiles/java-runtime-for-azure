package com.azure.runtime.host.extensions.azure.storage.resources;

import com.azure.runtime.host.resources.Resource;
import com.azure.runtime.host.resources.ResourceType;
import com.azure.runtime.host.resources.traits.ResourceWithConnectionString;
import com.azure.runtime.host.resources.traits.ResourceWithParent;

public abstract class AzureStorageChildResource extends Resource<AzureStorageChildResource>
                                                implements ResourceWithConnectionString<AzureStorageChildResource>,
                                                  ResourceWithParent<AzureStorageResource> {
    final AzureStorageResource storageResource;
//    private final String endpointSuffix;
//    private final ReferenceExpression connectionStringExpression;

    AzureStorageChildResource(String name, AzureStorageResource storageResource) {
        super(ResourceType.VALUE, name);
        this.storageResource = storageResource;
//        this.endpointSuffix = endpointSuffix;
//        this.connectionStringExpression = connectionStringExpression;
    }

    @Override
    public AzureStorageResource getParent() {
        return storageResource;
    }

//    @Override
//    public String getConnectionStringEnvironmentVariable() {
//        return "connectionString";
//    }



//    @Override
//    public String getValue() {
//        // FIXME this kind of concatenation is error prone
//        return "{" + storageResource.getName() + ".outputs." + endpointSuffix + "}";
//    }

    @Override
    public AzureStorageChildResource self() {
        return this;
    }
}

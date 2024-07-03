package com.microsoft.aspire.extensions.azure.storage;

import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.Extension;
import com.microsoft.aspire.extensions.azure.storage.resources.AzureStorageBlobsResource;
import com.microsoft.aspire.extensions.azure.storage.resources.AzureStorageResource;
import com.microsoft.aspire.resources.Resource;

import java.util.List;

public class AzureStorageExtension implements Extension {

    @Override
    public String getName() {
        return "Azure Storage";
    }

    @Override
    public String getDescription() {
        return "Provides resources for Azure Storage";
    }

    @Override
    public List<Class<? extends Resource<?>>> getAvailableResources() {
        return List.of(AzureStorageResource.class, AzureStorageBlobsResource.class);
    }

    public AzureStorageResource addAzureStorage(String name) {
        return DistributedApplication.getInstance().addResource(new AzureStorageResource(name));
    }
}

package com.azure.runtime.host.extensions.azure.storage;

import com.azure.runtime.host.extensions.azure.storage.resources.AzureStorageResource;
import com.azure.runtime.host.DistributedApplication;
import com.azure.runtime.host.Extension;

public class AzureStorageExtension implements Extension {

    @Override
    public String getName() {
        return "Azure Storage";
    }

    @Override
    public String getDescription() {
        return "Provides resources for Azure Storage.";
    }

    public AzureStorageResource addAzureStorage(String name) {
        return DistributedApplication.getInstance().addResource(new AzureStorageResource(name));
    }
}

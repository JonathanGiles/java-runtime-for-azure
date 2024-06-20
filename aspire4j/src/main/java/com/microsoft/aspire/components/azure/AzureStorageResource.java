package com.microsoft.aspire.components.azure;

import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.implementation.DistributedApplicationHelper;

public final class AzureStorageResource extends AzureBicepResource {

    public AzureStorageResource(String name) {
        super(name, "storage.module.bicep");

        withParameter("principalId", "");
        withParameter("principalType", "");
    }

    public AzureStorageResource addBlobs(String name) {
        DistributedApplicationHelper.getDistributedApplication()
                .addValue(name, "connectionString", "{storage.output.blobEndpoint}");
        return this;
    }
}

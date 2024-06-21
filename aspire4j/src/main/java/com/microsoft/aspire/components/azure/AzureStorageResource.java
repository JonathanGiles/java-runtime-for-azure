package com.microsoft.aspire.components.azure;

import com.microsoft.aspire.components.common.traits.ResourceWithConnectionString;
import com.microsoft.aspire.implementation.DistributedApplicationHelper;

public final class AzureStorageResource extends AzureBicepResource
                                        implements ResourceWithConnectionString<AzureStorageResource> {

    public AzureStorageResource(String name) {
        super(name, "com/microsoft/aspire/storage.module.bicep");

        withParameter("principalId", "");
        withParameter("principalType", "");
    }

    public AzureStorageResource addBlobs(String name) {
        DistributedApplicationHelper.getDistributedApplication()
                .addValue(name, "connectionString", getConnectionString());
        return this;
    }

    @Override
    public String getConnectionString() {
        return "{storage.output.blobEndpoint}";
    }

    @Override
    public String getConnectionStringEnvironmentVariable() {
        return "{blobs.connectionString}";
    }
}

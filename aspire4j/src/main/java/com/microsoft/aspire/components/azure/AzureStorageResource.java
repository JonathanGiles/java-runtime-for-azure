package com.microsoft.aspire.components.azure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microsoft.aspire.components.common.traits.ResourceWithConnectionString;
import com.microsoft.aspire.implementation.DistributedApplicationHelper;

public final class AzureStorageResource extends AzureBicepResource
                                        implements ResourceWithConnectionString<AzureStorageResource> {

    public AzureStorageResource(String name) {
        super(name, "storage.module.bicep");

        withParameter("principalId", "");
        withParameter("principalType", "");
    }

    public AzureStorageBlobsResource addBlobs(String name) {
        return DistributedApplicationHelper.getDistributedApplication().addValue(
                new AzureStorageBlobsResource(name, getConnectionString()));
    }

    @Override
    @JsonIgnore
    public String getConnectionString() {
        // FIXME we've hardcoded the bicep output that we know
        return "{storage.outputs.blobEndpoint}";
    }
}

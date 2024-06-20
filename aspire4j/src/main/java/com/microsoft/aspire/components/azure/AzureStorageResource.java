package com.microsoft.aspire.components.azure;

import com.microsoft.aspire.components.common.Resource;

public final class AzureStorageResource extends AzureBicepResource {

    public AzureStorageResource(String name) {
        super("azure.bicep.v0", name);

        withParameter("principalId", "");
        withParameter("principalType", "");
    }

    public AzureStorageResource addBlobs(String blobs) {
        // TODO register blobs
        return this;
    }
}

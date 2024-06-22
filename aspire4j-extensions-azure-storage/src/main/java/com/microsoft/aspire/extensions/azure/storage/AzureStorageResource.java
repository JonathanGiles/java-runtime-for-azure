package com.microsoft.aspire.extensions.azure.storage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microsoft.aspire.resources.AzureBicepResource;
import com.microsoft.aspire.resources.traits.ResourceWithConnectionString;
import com.microsoft.aspire.DistributedApplicationHelper;

public class AzureStorageResource extends AzureBicepResource
                                  implements ResourceWithConnectionString<AzureStorageResource> {

    public AzureStorageResource(String name) {
        super(name, "storage.module.bicep");

        // FIXME just here because I saw other samples with it
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

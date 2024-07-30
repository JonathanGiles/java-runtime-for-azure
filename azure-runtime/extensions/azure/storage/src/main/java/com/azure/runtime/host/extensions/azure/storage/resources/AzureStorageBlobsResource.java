package com.azure.runtime.host.extensions.azure.storage.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.azure.runtime.host.resources.references.ReferenceExpression;

public final class AzureStorageBlobsResource extends AzureStorageChildResource {

    public AzureStorageBlobsResource(String name, AzureStorageResource storageResource) {
        super(name, storageResource);
    }

    @Override
    public ReferenceExpression getConnectionStringExpression() {
        // FIXME duplicated below
        return ReferenceExpression.create("{" + storageResource.getName() + ".outputs.blobEndpoint}");
    }

    @JsonIgnore
    @Override
    public String getValueExpression() {
        // FIXME
        return "{" + storageResource.getName() + ".outputs.blobEndpoint}";
    }
}

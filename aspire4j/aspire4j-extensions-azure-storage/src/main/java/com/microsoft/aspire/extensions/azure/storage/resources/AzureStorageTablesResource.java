package com.microsoft.aspire.extensions.azure.storage.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microsoft.aspire.resources.properties.ReferenceExpression;

public final class AzureStorageTablesResource extends AzureStorageChildResource {

    public AzureStorageTablesResource(String name, AzureStorageResource storageResource) {
        super(name, storageResource);
    }

    @Override
    public ReferenceExpression getConnectionStringExpression() {
        // FIXME duplicated below
        return ReferenceExpression.create("{" + storageResource.getName() + ".outputs.tableEndpoint}");
    }

    @JsonIgnore
    @Override
    public String getValueExpression() {
        // FIXME
        return "{" + storageResource.getName() + ".outputs.tableEndpoint}";
    }
}

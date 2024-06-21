package com.microsoft.aspire.components.azure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microsoft.aspire.components.common.Value;
import com.microsoft.aspire.components.common.traits.ResourceWithConnectionString;

public final class AzureStorageBlobsResource extends Value
        implements ResourceWithConnectionString<AzureStorageBlobsResource>{

    public AzureStorageBlobsResource(String name, String connectionString) {
        super(name, "connectionString", connectionString);
    }

    @Override
    @JsonIgnore
    public String getConnectionString() {
        // FIXME we've hardcoded the bicep output that we know
        return "{storage.outputs.blobEndpoint}";
    }
}

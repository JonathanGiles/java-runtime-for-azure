package com.microsoft.aspire.extensions.azure.storage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microsoft.aspire.resources.AzureBicepResource;
import com.microsoft.aspire.resources.traits.ResourceWithConnectionString;
import com.microsoft.aspire.DistributedApplicationHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.microsoft.aspire.extensions.azure.storage.AzureStorageExtension.AZURE_STORAGE;

public class AzureStorageResource extends AzureBicepResource
                                  implements ResourceWithConnectionString<AzureStorageResource> {
    private static final String BICEP_PATH = "storage.module.bicep";

    public AzureStorageResource(String name) {
        super(AZURE_STORAGE, name, BICEP_PATH);

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

    @Override
    public List<BicepFileOutput> getBicepFiles() {
        // read the file from our local resources directory
        InputStream resourceAsStream = AzureStorageResource.class.getResourceAsStream(BICEP_PATH);
        try {
            String bicepTemplate = new String(resourceAsStream.readAllBytes());

            // If necessary, modify template variables

            return List.of(new BicepFileOutput(BICEP_PATH, bicepTemplate));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.microsoft.aspire.extensions.azure.storage.resources;

import com.microsoft.aspire.resources.AzureBicepResource;
import com.microsoft.aspire.resources.ResourceType;
import com.microsoft.aspire.resources.properties.EndpointReference;
import com.microsoft.aspire.DistributedApplicationHelper;
import com.microsoft.aspire.resources.traits.ResourceWithEndpoints;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AzureStorageResource extends AzureBicepResource<AzureStorageResource>
                                  implements ResourceWithEndpoints<AzureStorageResource> {
    private static final ResourceType AZURE_STORAGE = ResourceType.fromString("azure.storage.v0");

    private static final String BICEP_TEMPLATE_FILE = "templates/bicep/storage.module.bicep";
    private static final String BICEP_OUTPUT_FILE = "%s.module.bicep";

    private final String bicepOutputFilename;

    public AzureStorageResource(String name) {
        super(AZURE_STORAGE, name);

        // This is the path to the output bicep file
        bicepOutputFilename = String.format(BICEP_OUTPUT_FILE, name);
        withPath(bicepOutputFilename);

        // FIXME just here because I saw other samples with it
//        withParameter("principalId", "");
//        withParameter("principalType", "");
    }

    public AzureStorageBlobsResource addBlobs(String name) {
        return DistributedApplicationHelper.getDistributedApplication().addValue(
                new AzureStorageBlobsResource(name, this)); // "{images.outputs.blobEndpoint}"
    }

    @Override
    public List<EndpointReference> getEndpoints() {
        // TODO how do I know which endpoints are available?
        return List.of();
    }

    @Override
    public List<TemplateFileOutput> processTemplate() {
        // read the file from our local resources directory
        InputStream resourceAsStream = AzureStorageResource.class.getResourceAsStream(BICEP_TEMPLATE_FILE);
        if (resourceAsStream == null) {
            throw new RuntimeException("Resource file not found: " + BICEP_TEMPLATE_FILE);
        }
        try {
            String bicepTemplate = new String(resourceAsStream.readAllBytes());

            // If necessary, modify template variables

            return List.of(new TemplateFileOutput(bicepOutputFilename, bicepTemplate));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

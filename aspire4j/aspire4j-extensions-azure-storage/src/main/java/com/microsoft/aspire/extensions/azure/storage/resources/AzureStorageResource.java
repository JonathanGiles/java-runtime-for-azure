package com.microsoft.aspire.extensions.azure.storage.resources;

import com.microsoft.aspire.resources.AzureBicepResource;
import com.microsoft.aspire.resources.ResourceType;
import com.microsoft.aspire.resources.properties.EndpointReference;
import com.microsoft.aspire.DistributedApplicationHelper;
import com.microsoft.aspire.resources.traits.ResourceWithEndpoints;
import com.microsoft.aspire.utils.templates.TemplateEngine;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class AzureStorageResource extends AzureBicepResource<AzureStorageResource>
                                  implements ResourceWithEndpoints<AzureStorageResource> {
    private static final ResourceType AZURE_STORAGE = ResourceType.fromString("azure.storage.v0");

    public AzureStorageResource(String name) {
        super(AZURE_STORAGE, name);

        // This is the path to the output bicep file
//        bicepOutputFilename = String.format(BICEP_OUTPUT_FILE, name);
//        withPath(bicepOutputFilename);

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
    public List<TemplateFileOutput> processTemplate(Path outputPath) {
        List<TemplateDescriptor> templateFiles = List.of(
            new TemplateDescriptor("/templates/bicep/storage.module.bicep", "${name}.module.bicep")
        );

        List<TemplateFileOutput> templateOutput = TemplateEngine.process(AzureStorageResource.class, templateFiles, Map.of(
            "name", getName()
        ));

        // we know that we need to get the output filename from the first element, and set that as the path
        withPath(outputPath.toString());

        return templateOutput;
    }
}

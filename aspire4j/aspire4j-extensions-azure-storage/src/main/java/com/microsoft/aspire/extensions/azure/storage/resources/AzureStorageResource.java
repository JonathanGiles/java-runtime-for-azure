package com.microsoft.aspire.extensions.azure.storage.resources;

import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.resources.AzureBicepResource;
import com.microsoft.aspire.resources.ResourceType;
import com.microsoft.aspire.resources.properties.EndpointReference;
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
    }

    public AzureStorageBlobsResource addBlobs(String name) {
        return DistributedApplication.getInstance().addValue(new AzureStorageBlobsResource(name, this));
    }

    @Override
    public List<EndpointReference> getEndpoints() {
        // TODO how do I know which endpoints are available?
        return List.of();
    }

    @Override
    public List<TemplateFileOutput> processTemplate(Path outputPath) {
        final String templatePath = "/templates/bicep/";
        final String outputRootPath = "";
        List<TemplateDescriptor> templateFiles = TemplateDescriptorsBuilder.begin(templatePath, outputRootPath)
            .with("storage.module.bicep", "${name}.module.bicep")
            .build();

        List<TemplateFileOutput> templateOutput = TemplateEngine.process(AzureStorageResource.class, templateFiles, Map.of(
            "name", getName()
        ));

        // we know that we need to get the output filename from the first element, and set that as the path
        // FIXME we need a better way of determining the output path of the template
        withPath(templateOutput.get(0).filename());

        return templateOutput;
    }
}

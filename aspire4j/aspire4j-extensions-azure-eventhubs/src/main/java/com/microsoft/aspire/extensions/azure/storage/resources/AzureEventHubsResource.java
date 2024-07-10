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

public class AzureEventHubsResource extends AzureBicepResource<AzureEventHubsResource>
                                  implements ResourceWithEndpoints<AzureEventHubsResource> {

    public AzureEventHubsResource(String name) {
        super(name);
    }

//    public AzureEventHubsResource addEventHub(String name) {
//        return DistributedApplication.getInstance().addValue(new AzureEventHubsResource(name, this));
//    }

    @Override
    public List<TemplateFileOutput> processTemplate() {
        final String templatePath = "/templates/bicep/";
        List<TemplateDescriptor> templateFiles = TemplateDescriptorsBuilder.begin(templatePath)
            .with("eventhubns.module.bicep", "${name}.module.bicep")
            .build();

        List<TemplateFileOutput> templateOutput = TemplateEngine.process(AzureEventHubsResource.class, templateFiles, Map.of(
            "name", getName()
        ));

        // we know that we need to get the output filename from the first element, and set that as the path
        // FIXME we need a better way of determining the output path of the template
        withPath(templateOutput.get(0).filename());

        return templateOutput;
    }
}

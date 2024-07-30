package com.azure.runtime.host.extensions.azure.eventhubs.resources;

import com.azure.runtime.host.resources.AzureBicepResource;
import com.azure.runtime.host.resources.traits.ResourceWithEndpoints;
import com.azure.runtime.host.utils.templates.TemplateDescriptor;
import com.azure.runtime.host.utils.templates.TemplateDescriptorsBuilder;
import com.azure.runtime.host.utils.templates.TemplateEngine;
import com.azure.runtime.host.utils.templates.TemplateFileOutput;

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

        List<TemplateFileOutput> templateOutput = TemplateEngine.getTemplateEngine()
            .process(AzureEventHubsResource.class, templateFiles, Map.of("name", getName()));

        // we know that we need to get the output filename from the first element, and set that as the path
        // FIXME we need a better way of determining the output path of the template
        withPath(templateOutput.get(0).filename());

        return templateOutput;
    }
}

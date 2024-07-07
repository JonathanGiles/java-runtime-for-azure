package com.microsoft.aspire.extensions.azure.openai.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microsoft.aspire.resources.AzureBicepResource;
import com.microsoft.aspire.resources.properties.EndpointReference;
import com.microsoft.aspire.resources.traits.ResourceWithConnectionString;
import com.microsoft.aspire.resources.traits.ResourceWithEndpoints;
import com.microsoft.aspire.utils.templates.TemplateEngine;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AzureOpenAIResource extends AzureBicepResource<AzureOpenAIResource>
                                  implements ResourceWithEndpoints<AzureOpenAIResource>,
                                             ResourceWithConnectionString<AzureOpenAIResource> {

    @JsonIgnore
    private final List<AzureOpenAIDeployment> deployments;

    public AzureOpenAIResource(String name) {
        super(name);
        this.deployments = new ArrayList<>();
        withParameter("principalId", "");
        withParameter("principalType", "");
    }

    // TODO the actual intent of this deployment list is to properly write out the deployment resources in the bicep
    //  template. For now we are not doing that, and simply using a pre-baked bicep template.
    public AzureOpenAIResource withDeployment(AzureOpenAIDeployment deployment) {
        this.deployments.add(deployment);
        return this;
    }

    @Override
    public List<EndpointReference> getEndpoints() {
        // TODO how do I know which endpoints are available?
        return List.of();
    }

    @Override
    public List<TemplateFileOutput> processTemplate(Path outputPath) {
        final String templatePath = "/templates/openai/bicep/";
        final String outputRootPath = "";
        List<TemplateDescriptor> templateFiles = TemplateDescriptorsBuilder.begin(templatePath, outputRootPath)
            .with("openai.module.bicep", "${name}.module.bicep")
            .build();

        List<TemplateFileOutput> templateOutput = TemplateEngine.process(AzureOpenAIResource.class, templateFiles, Map.of(
            "name", getName()
        ));

        // we know that we need to get the output filename from the first element, and set that as the path
        // FIXME we need a better way of determining the output path of the template
        withPath(templateOutput.get(0).filename());

        return templateOutput;
    }

    @Override
    public String getConnectionStringEnvironmentVariable() {
        return "connectionString";
    }

    @Override
    public String getValue() {
        return "{" + getName() + ".outputs.connectionString}";
    }
}

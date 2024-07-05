package com.microsoft.aspire.extensions.azure.openai;

import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.Extension;
import com.microsoft.aspire.extensions.azure.openai.resources.AzureOpenAIResource;

public class AzureOpenAIExtension implements Extension {

    @Override
    public String getName() {
        return "Azure OpenAI";
    }

    @Override
    public String getDescription() {
        return "Provides resources for Azure OpenAI";
    }

    public AzureOpenAIResource addAzureOpenAI(String name) {
        return DistributedApplication.getInstance().addResource(new AzureOpenAIResource(name));
    }
}

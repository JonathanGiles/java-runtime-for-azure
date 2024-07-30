package com.azure.runtime.host.extensions.azure.openai;

import com.azure.runtime.host.extensions.azure.openai.resources.AzureOpenAIResource;
import com.azure.runtime.host.DistributedApplication;
import com.azure.runtime.host.Extension;

public class AzureOpenAIExtension implements Extension {

    @Override
    public String getName() {
        return "Azure OpenAI";
    }

    @Override
    public String getDescription() {
        return "Provides resources for Azure OpenAI.";
    }

    public AzureOpenAIResource addAzureOpenAI(String name) {
        return DistributedApplication.getInstance().addResource(new AzureOpenAIResource(name));
    }
}

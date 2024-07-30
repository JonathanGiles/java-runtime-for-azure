package com.azure.runtime.host.extensions.azure.openai.resources;

/**
 * Represents an Azure OpenAI Deployment.
 */
public record AzureOpenAIDeployment(
    String name, String modelName, String modelVersion, String skuName, int skuCapacity) {

    // Static factory method for creating an instance with some default values
    public static AzureOpenAIDeployment using(String name, String modelName, String modelVersion) {
        return using(name, modelName, modelVersion, "Standard", 1);
    }

    public static AzureOpenAIDeployment using(String name, String modelName, String modelVersion, String skuName, int skuCapacity) {
        return new AzureOpenAIDeployment(name, modelName, modelVersion, skuName, skuCapacity);
    }
}

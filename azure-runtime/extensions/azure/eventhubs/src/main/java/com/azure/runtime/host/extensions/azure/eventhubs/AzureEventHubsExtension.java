package com.azure.runtime.host.extensions.azure.eventhubs;

import com.azure.runtime.host.extensions.azure.eventhubs.resources.AzureEventHubsResource;
import com.azure.runtime.host.DistributedApplication;
import com.azure.runtime.host.Extension;

public class AzureEventHubsExtension implements Extension {

    @Override
    public String getName() {
        return "Azure Event Hubs";
    }

    @Override
    public String getDescription() {
        return "Provides resources for Azure Event Hubs";
    }

    /**
     * Adds an Azure Event Hubs Namespace resource to the application model. This resource can be used to create Event
     * Hub resources.
     * @param name
     * @return
     */
    public AzureEventHubsResource addAzureEventHubs(String name) {
        return DistributedApplication.getInstance().addResource(new AzureEventHubsResource(name));
    }
}

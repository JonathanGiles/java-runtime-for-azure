package com.microsoft.aspire.extensions.microservice.common;

import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.Extension;
import com.microsoft.aspire.extensions.microservice.common.resources.EurekaServiceDiscovery;

public abstract class MicroserviceExtension implements Extension {
    private final String name;
    private final String description;

    protected MicroserviceExtension(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Adds a new Eureka service to the app host.
     * @param name The name of the Eureka service.
     * @return A new {@link EurekaServiceDiscovery} instance that can be used to configure Eureka.
     */
    public EurekaServiceDiscovery addEurekaServiceDiscovery(String name) {
        return DistributedApplication.getInstance().addResource(new EurekaServiceDiscovery(name));
    }
}

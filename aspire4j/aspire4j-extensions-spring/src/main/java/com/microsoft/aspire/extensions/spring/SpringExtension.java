package com.microsoft.aspire.extensions.spring;

import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.Extension;
import com.microsoft.aspire.extensions.spring.resources.EurekaServiceDiscovery;
import com.microsoft.aspire.extensions.spring.resources.SpringProject;
import com.microsoft.aspire.resources.Resource;

import java.util.List;

public class SpringExtension implements Extension {

    @Override
    public String getName() {
        return "Spring";
    }

    @Override
    public String getDescription() {
        return "Provides support for working with Spring applications";
    }

    /**
     * Adds a new Spring project to the app host.
     * @param name The name of the spring project.
     * @return A new {@link SpringProject} instance that can be used to configure the project.
     */
    public SpringProject addSpringProject(String name) {
        return DistributedApplication.getInstance().addResource(new SpringProject(name));
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

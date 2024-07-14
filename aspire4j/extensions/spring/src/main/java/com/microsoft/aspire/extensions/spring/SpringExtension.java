package com.microsoft.aspire.extensions.spring;

import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.extensions.microservice.common.MicroserviceExtension;
import com.microsoft.aspire.extensions.spring.resources.SpringProject;

public class SpringExtension extends MicroserviceExtension {
    public SpringExtension() {
        super("Spring", "Provides support for working with Spring applications.");
    }

    /**
     * Adds a new Spring project to the app host.
     * @param name The name of the spring project.
     * @return A new {@link SpringProject} instance that can be used to configure the project.
     */
    public SpringProject addSpringProject(String name) {
        return DistributedApplication.getInstance().addResource(new SpringProject(name));
    }
}

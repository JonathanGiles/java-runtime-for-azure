package com.azure.runtime.host.extensions.quarkus;

import com.azure.runtime.host.DistributedApplication;
import com.azure.runtime.host.extensions.quarkus.resources.QuarkusProject;
import com.azure.runtime.host.extensions.microservice.common.MicroserviceExtension;

public class QuarkusExtension extends MicroserviceExtension {
    public QuarkusExtension() {
        super("Quarkus", "Provides support for working with Quarkus applications.");
    }

    /**
     * Adds a new Quarkus project to the app host.
     * @param name The name of the Quarkus project.
     * @return A new {@link QuarkusProject} instance that can be used to configure the project.
     */
    public QuarkusProject addQuarkusProject(String name) {
        return DistributedApplication.getInstance().addResource(new QuarkusProject(name));
    }
}

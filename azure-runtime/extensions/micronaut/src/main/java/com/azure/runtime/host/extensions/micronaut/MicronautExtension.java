package com.azure.runtime.host.extensions.micronaut;

import com.azure.runtime.host.DistributedApplication;
import com.azure.runtime.host.extensions.micronaut.resources.MicronautProject;
import com.azure.runtime.host.extensions.microservice.common.MicroserviceExtension;

public class MicronautExtension extends MicroserviceExtension {
    public MicronautExtension() {
        super("Micronaut", "Provides support for working with Micronaut applications.");
    }

    /**
     * Adds a new Micronaut project to the app host.
     * @param name The name of the Micronaut project.
     * @return A new {@link MicronautProject} instance that can be used to configure the project.
     */
    public MicronautProject addMicronautProject(String name) {
        return DistributedApplication.getInstance().addResource(new MicronautProject(name));
    }
}

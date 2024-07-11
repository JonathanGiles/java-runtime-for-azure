package com.microsoft.aspire.extensions.dotnet;

import com.microsoft.aspire.DistributedApplication;
import com.microsoft.aspire.Extension;
import com.microsoft.aspire.extensions.dotnet.resources.Project;

public class DotnetExtension implements Extension {

    @Override
    public String getName() {
        return "Microsoft .NET";
    }

    @Override
    public String getDescription() {
        return "Provides support for working with .NET applications.";
    }

    /**
     * Add a new project to the distributed application.
     *
     * @param project
     * @return
     * @param <T>
     */
    public <T extends Project<?>> T addProject(T project) {
        return DistributedApplication.getInstance().addResource(project);
    }

    /**
     * Add a new project to the distributed application.
     *
     * @param name
     * @return
     */
    public Project<?> addProject(String name) {
        return DistributedApplication.getInstance().addResource(new Project<>(name));
    }
}

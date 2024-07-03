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

    @Override
    public List<Class<? extends Resource>> getAvailableResources() {
        return List.of(SpringProject.class);
    }

    public SpringProject addSpringProject(String name) {
        return DistributedApplication.getInstance().addResource(new SpringProject(name));
    }

    public EurekaServiceDiscovery addEurekaServiceDiscovery(String name) {
        return DistributedApplication.getInstance().addResource(new EurekaServiceDiscovery(name));
    }
}

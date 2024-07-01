package com.microsoft.aspire.extensions.spring;

import com.microsoft.aspire.DistributedApplicationHelper;
import com.microsoft.aspire.Extension;
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
        return DistributedApplicationHelper.getDistributedApplication().addResource(new SpringProject(name));
    }
}

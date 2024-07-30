package manifold.spring.extensions.com.azure.runtime.host.DistributedApplication;

import com.azure.runtime.host.extensions.microservice.common.resources.EurekaServiceDiscovery;
import com.azure.runtime.host.extensions.spring.resources.SpringProject;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import com.azure.runtime.host.DistributedApplication;

@Extension
public class SpringDistributedApplication {

  public static SpringProject addSpringProject(@This DistributedApplication app, String name) {
    return app.addResource(new SpringProject(name));
  }

  public static EurekaServiceDiscovery addEurekaServiceDiscovery(@This DistributedApplication app, String name) {
    return app.addResource(new EurekaServiceDiscovery(name));
  }
}
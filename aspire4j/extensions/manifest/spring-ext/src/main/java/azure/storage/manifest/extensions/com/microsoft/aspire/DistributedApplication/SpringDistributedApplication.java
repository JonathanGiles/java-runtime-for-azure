package azure.storage.manifest.extensions.com.microsoft.aspire.DistributedApplication;

import com.microsoft.aspire.extensions.microservice.common.resources.EurekaServiceDiscovery;
import com.microsoft.aspire.extensions.spring.resources.SpringProject;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import com.microsoft.aspire.DistributedApplication;

@Extension
public class SpringDistributedApplication {

  public static SpringProject addSpringProject(@This DistributedApplication app, String name) {
    return app.addResource(new SpringProject(name));
  }

  public static EurekaServiceDiscovery addEurekaServiceDiscovery(@This DistributedApplication app, String name) {
    return app.addResource(new EurekaServiceDiscovery(name));
  }
}
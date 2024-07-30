package manifold.azure.storage.extensions.com.azure.runtime.host.DistributedApplication;

import com.azure.runtime.host.extensions.azure.storage.resources.AzureStorageResource;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import com.azure.runtime.host.DistributedApplication;

@Extension
public class AzureStorageDistributedApplication {

  public static AzureStorageResource addAzureStorage(@This DistributedApplication app, String name) {
    return app.addResource(new AzureStorageResource(name));
  }
}
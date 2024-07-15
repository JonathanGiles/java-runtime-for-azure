package azure.storage.manifest.extensions.com.microsoft.aspire.DistributedApplication;

import com.microsoft.aspire.extensions.azure.storage.resources.AzureStorageResource;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import com.microsoft.aspire.DistributedApplication;

@Extension
public class AzureStorageDistributedApplication {

  public static AzureStorageResource addAzureStorage(@This DistributedApplication app, String name) {
    return app.addResource(new AzureStorageResource(name));
  }
}
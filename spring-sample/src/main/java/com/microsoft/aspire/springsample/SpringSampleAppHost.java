package com.microsoft.aspire.springsample;

import com.microsoft.aspire.AppHost;
import com.microsoft.aspire.DistributedApplication;

public class SpringSampleAppHost implements AppHost {

    public static void main(String[] args) {
        new SpringSampleAppHost().run();
    }

    @Override
    public void configureApplication(DistributedApplication app) {
        // Inspired by:
        // https://github.com/dotnet/aspire/blob/main/playground/AzureStorageEndToEnd/AzureStorageEndToEnd.AppHost/Program.cs
        var azureStorage = app.addAzureStorage("storage");
        var blobStorage = azureStorage.addBlobs("blobs");

        app.addProject("spring-sample")
            .withExternalHttpEndpoints()
            .withReference(blobStorage);

        app.addRedis("redis")
            .withDataVolume()
            .withEnvironment("foo", "bar");

        // TODO Wire up the aspire-dashboard container
        app.addContainer("aspire-dashboard", "TODO");
    }
}

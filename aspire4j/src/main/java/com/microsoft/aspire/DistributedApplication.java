package com.microsoft.aspire;

import com.microsoft.aspire.components.azure.AzureStorageResource;
import com.microsoft.aspire.components.common.Container;
import com.microsoft.aspire.components.common.Project;
import com.microsoft.aspire.implementation.manifest.AspireManifest;

public class DistributedApplication {

    final AspireManifest manifest;

    DistributedApplication() {
        manifest = new AspireManifest();
    }

    public AzureStorageResource addAzureStorage(String name) {
        return manifest.addResource(new AzureStorageResource(name));
    }

    public Project addProject(String name) {
        return manifest.addResource(new Project(name));
    }

    /***************************************************************************
     *
     * CONTAINERS
     *
     **************************************************************************/
    public Container addContainer(String name, String image) {
        return manifest.addResource(new Container(name, image));
    }

    public Container addRedis(String name) {
        return addContainer(name, "docker.io/library/redis:7.2");
    }
}
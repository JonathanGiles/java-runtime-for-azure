package com.microsoft.aspire;

import com.microsoft.aspire.components.azure.AzureStorageResource;
import com.microsoft.aspire.components.common.*;
import com.microsoft.aspire.components.common.properties.Binding;
import com.microsoft.aspire.implementation.DistributedApplicationHelper;
import com.microsoft.aspire.implementation.manifest.AspireManifest;
import jakarta.validation.Valid;

public class DistributedApplication {

    @Valid
    final AspireManifest manifest;

    DistributedApplication() {
        manifest = new AspireManifest();

        // FIXME We could make this a static field, but it all feels hacky, so we will go with this for now
        DistributedApplicationHelper.setAccessor(() -> this);
    }


    /***************************************************************************
     *
     * Project
     *
     **************************************************************************/

    public <T extends Project> T addProject(T project) {
        return manifest.addResource(project);
    }

    public Project addProject(String name) {
        return manifest.addResource(new Project(name));
    }


    /***************************************************************************
     *
     * DockerFile
     *
     **************************************************************************/

    public <T extends DockerFile> T addDockerFile(T dockerFile) {
        return manifest.addResource(dockerFile);
    }

    public DockerFile addDockerFile(String name, String path) {
        return addDockerFile(name, path, ".");
    }

    public DockerFile addDockerFile(String name, String path, String context) {
        return manifest.addResource(new DockerFile(name, path, context));
    }


    /***************************************************************************
     *
     * Container
     *
     **************************************************************************/

    public <T extends Container> T addContainer(T container) {
        return manifest.addResource(container);
    }

    public Container addContainer(String name, String image) {
        return manifest.addResource(new Container(name, image));
    }


    /***************************************************************************
     *
     * Executable
     *
     **************************************************************************/

    public <T extends Executable> T addExecutable(T executable) {
        return manifest.addResource(executable);
    }

    public Executable addExecutable(String name, String command, String workingDirectory, String... args) {
        return manifest.addResource(new Executable(name, command, workingDirectory).withArguments(args));
    }


    /***************************************************************************
     *
     * Value
     *
     **************************************************************************/

    public <T extends Value> T addValue(T value) {
        return manifest.addResource(value);
    }

    public Value addValue(String name, String key, String value) {
        return manifest.addResource(new Value(name, key, value));
    }

    /***************************************************************************
     *
     * 'Extensions'
     *
     **************************************************************************/

    // demo of redis support being baked in
    public Container addRedis(String name) {
        return addContainer(name, "docker.io/library/redis:7.2");
    }

    public Container addAspireDashboard() {
        // from https://learn.microsoft.com/en-us/dotnet/aspire/fundamentals/dashboard/standalone?tabs=bash
        // FIXME creating a binding is too verbose
        return addContainer("aspire-dashboard", "mcr.microsoft.com/dotnet/aspire-dashboard:8.0.0")
                .withBinding(new Binding(Binding.Scheme.TCP, Binding.Protocol.TCP, Binding.Transport.HTTP)
                        .withPort(4317)
                        .withTargetPort(18889))
                .withBinding(new Binding(Binding.Scheme.HTTP, Binding.Protocol.TCP, Binding.Transport.HTTP)
                        .withPort(18888)
                        .withTargetPort(18888));
    }

    public AzureStorageResource addAzureStorage(String name) {
        return manifest.addResource(new AzureStorageResource(name));
    }
}
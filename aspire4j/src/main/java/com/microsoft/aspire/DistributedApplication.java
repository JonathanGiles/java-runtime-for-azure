package com.microsoft.aspire;

import com.microsoft.aspire.resources.*;
import com.microsoft.aspire.resources.properties.Binding;
import com.microsoft.aspire.implementation.manifest.AspireManifest;
import jakarta.validation.Valid;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class DistributedApplication {

    @Valid
    final AspireManifest manifest;

    private final List<Extension> extensions = new ArrayList<>();

    DistributedApplication() {
        manifest = new AspireManifest();
        loadExtensions();

        // FIXME We could make this a static field, but it all feels hacky, so we will go with this for now
        DistributedApplicationHelper.setAccessor(() -> this);
    }

    private void loadExtensions() {
        ServiceLoader.load(Extension.class).forEach(extensions::add);
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

//    // demo of redis support being baked in
//    public Container addRedis(String name) {
//        return addContainer(name, "docker.io/library/redis:7.2");
//    }
//
//    public Container addAspireDashboard() {
//        // from https://learn.microsoft.com/en-us/dotnet/aspire/fundamentals/dashboard/standalone?tabs=bash
//        // FIXME creating a binding is too verbose
//        return addContainer("aspire-dashboard", "mcr.microsoft.com/dotnet/aspire-dashboard:8.0.0")
//                .withBinding(new Binding(Binding.Scheme.TCP, Binding.Protocol.TCP, Binding.Transport.HTTP)
//                        .withPort(4317)
//                        .withTargetPort(18889))
//                .withBinding(new Binding(Binding.Scheme.HTTP, Binding.Protocol.TCP, Binding.Transport.HTTP)
//                        .withPort(18888)
//                        .withTargetPort(18888));
//    }

//    public AzureStorageResource addAzureStorage(String name) {
//        return manifest.addResource(new AzureStorageResource(name));
//    }

    // FIXME temporary
    public <T extends Resource> T addResource(T r) {
        return manifest.addResource(r);
    }

    public void printExtensions() {
        printExtensions(System.out);
    }

    public void printExtensions(PrintStream out) {
        out.println("Available Aspire4J Extensions:");
        extensions.forEach(e -> {
            out.println("  " + e.getName() + ": " + e.getDescription());
            e.getAvailableResources().forEach(r -> out.println("   - " + r.getSimpleName()));
        });
    }

    public <T extends Extension> T withExtension(Class<T> extension) {
        try {
            return extension.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create a new instance of the extension class", e);
        }
    }
}
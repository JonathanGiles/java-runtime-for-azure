package com.microsoft.aspire;

import com.microsoft.aspire.resources.*;
import jakarta.validation.Valid;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Represents the core of a distributed application within the Aspire4J framework. This class serves as the central
 * point for configuring and managing the various components and extensions that make up a distributed application.
 * <p>
 * The {@code DistributedApplication} class provides a fluent API for adding resources such as Docker containers,
 * executables, and values to the application. It also supports loading and configuring extensions that enhance
 * the application's capabilities beyond its core functionalities.
 * <p>
 * Instances of this class are typically created and configured within an {@link AppHost} implementation. The
 * {@code DistributedApplication} is designed to be a singleton within the context of an application's lifecycle,
 * ensuring a centralized configuration point.
 *
 * Usage example:
 *
 * {@snippet lang="java" :
 * public class MyAppHost implements AppHost {
 *
 *     public static void main(String[] args) {
 *         new MyAppHost().boot(args);
 *     }
 *
 *     @Override public void configureApplication(DistributedApplication app) {
 *         app.printExtensions();
 *         // ... the rest of the configuration
 *     }
 * }
 * }
 *
 * @see AppHost
 * @see Extension
 */
public class DistributedApplication {
    private static DistributedApplication INSTANCE;

    @Valid
    final AspireManifest manifest;

    private final List<Extension> extensions = new ArrayList<>();

    DistributedApplication() {
        manifest = new AspireManifest();
        loadExtensions();

        // FIXME This is hacky
        INSTANCE = this;
    }

    public static DistributedApplication getInstance() {
        return INSTANCE;
    }

    private void loadExtensions() {
        ServiceLoader.load(Extension.class).forEach(extensions::add);
    }


    /***************************************************************************
     *
     * Resource
     *
     **************************************************************************/

    /**
     * Adds a new resource to the distributed application. This method allows for the addition of various types of
     * resources that are essential for the application's operation, such as Docker files, containers, executables,
     * and values. Each resource added through this method is registered within the application's manifest, making it
     * a part of the application's configuration.
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * DockerFile<?> myDockerFile = new DockerFile<>("MyDockerFile", "./Dockerfile", ".");
     * DockerFile<?> addedDockerFile = app.addResource(myDockerFile);
     * }
     *
     * @param r   The resource to add to the application.
     * @param <T> The type of the resource being added, extending the {@link Resource} class.
     * @return The added resource, allowing for further configuration or chaining of operations.
     */
    public <T extends Resource<?>> T addResource(T r) {
        return manifest.addResource(r);
    }

    /**
     * Substitutes an existing resource with one or more new resources. This method is useful in scenarios where a
     * resource's configuration needs to be dynamically altered based on runtime conditions or other factors. Instead
     * of mutating the existing resource, which could lead to inconsistencies, this method ensures a clean replacement
     * by removing the old resource and adding the new ones.
     *
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * app.substituteResource(oldResource, newResource1, newResource2);
     * }
     *
     * @param oldResource The resource to remove.
     * @param newResources The resource(s) to add in the place of the old resource.
     */
    public void substituteResource(Resource<?> oldResource, Resource<?>... newResources) {
        manifest.substituteResource(oldResource, newResources);
    }


    /***************************************************************************
     *
     * DockerFile
     *
     **************************************************************************/

    /**
     * Adds a new DockerFile resource to the distributed application. DockerFiles are essential for defining the
     * environment in which containers will run. This method simplifies the process of adding DockerFiles to the
     * application's configuration.
     *
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * DockerFile<?> dockerFile = app.addDockerFile("myDockerFile", "./Dockerfile", ".");
     * }
     *
     * @param dockerFile The DockerFile to add.
     * @param <T> The type of the DockerFile.
     * @return The added DockerFile.
     */
    public <T extends DockerFile<?>> T addDockerFile(T dockerFile) {
        return manifest.addResource(dockerFile);
    }

    /**
     * Adds a new DockerFile resource to the distributed application using the specified parameters. This overload
     * provides a convenient way to add a DockerFile without creating an instance beforehand.
     *
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * DockerFile<?> dockerFile = app.addDockerFile("myDockerFile", "./Dockerfile", ".");
     * }
     *
     * @param name The name of the DockerFile.
     * @param path The path to the DockerFile.
     * @param context The build context.
     * @return The added DockerFile.
     */
    public DockerFile<?> addDockerFile(String name, String path, String context) {
        return manifest.addResource(new DockerFile<>(name, path, context));
    }


    /***************************************************************************
     *
     * Container
     *
     **************************************************************************/

    /**
     * Adds a new container resource to the distributed application. Containers are fundamental to the deployment and
     * execution of applications within a distributed system. This method facilitates the addition of container
     * configurations to the application.
     *
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * Container<?> container = app.addContainer("myContainer", "nginx:latest");
     * }
     *
     * @param container The container to add.
     * @param <T> The type of the container.
     * @return The added container.
     */
    public <T extends Container<?>> T addContainer(T container) {
        return manifest.addResource(container);
    }

    /**
     * Adds a new container resource to the distributed application using the specified parameters. This method
     * offers a straightforward way to add a container by specifying its name and image directly.
     *
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * Container<?> container = app.addContainer("myContainer", "nginx:latest");
     * }
     *
     * @param name The name of the container.
     * @param image The Docker image for the container.
     * @return The added container.
     */
    public Container<?> addContainer(String name, String image) {
        return manifest.addResource(new Container<>(name, image));
    }


    /***************************************************************************
     *
     * Executable
     *
     **************************************************************************/

    /**
     * Adds a new executable resource to the distributed application. Executables represent command-line applications
     * or scripts that are part of the application's operational requirements. This method allows for the inclusion of
     * such executables in the application's configuration.
     *
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * Executable<?> executable = app.addExecutable("myExecutable", "echo", "/usr/bin", "Hello, World!");
     * }
     *
     * @param executable The executable to add.
     * @param <T> The type of the executable.
     * @return The added executable.
     */
    public <T extends Executable<?>> T addExecutable(T executable) {
        return manifest.addResource(executable);
    }

    /**
     * Adds a new executable resource to the distributed application using the specified parameters. This convenience
     * method simplifies the process of adding executables by directly specifying their properties.
     *
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * Executable<?> executable = app.addExecutable("myExecutable", "echo", "/usr/bin", "Hello, World!");
     * }
     *
     * @param name The name of the executable.
     * @param command The command to execute.
     * @param workingDirectory The working directory for the executable.
     * @param args The arguments to pass to the executable.
     * @return The added executable.
     */
    public Executable<?> addExecutable(String name, String command, String workingDirectory, String... args) {
        return manifest.addResource(new Executable<>(name, command, workingDirectory).withArguments(args));
    }


    /***************************************************************************
     *
     * Value
     *
     **************************************************************************/

    /**
     * Adds a new value resource to the distributed application. Values are key-value pairs that can be used for
     * configuration purposes or as parameters for other resources. This method enables the addition of such values
     * to the application's configuration.
     *
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * Value<?> value = app.addValue("myValue", "key", "value");
     * }
     *
     * @param value The value to add.
     * @param <T> The type of the value.
     * @return The added value.
     */
    public <T extends Value<?>> T addValue(T value) {
        return manifest.addResource(value);
    }

    /**
     * Adds a new value resource to the distributed application using the specified parameters. This method provides
     * a direct way to add a key-value pair to the application's configuration.
     *
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * Value<?> value = app.addValue("myValue", "key", "value");
     * }
     *
     * @param name The name of the value.
     * @param key The key of the value.
     * @param value The value associated with the key.
     * @return The added value.
     */
    public Value<?> addValue(String name, String key, String value) {
        return manifest.addResource(new Value<>(name, key, value));
    }

    /***************************************************************************
     *
     * Extensions
     *
     **************************************************************************/

    /**
     * Prints the available extensions to the standard output. This method is useful for debugging and configuration
     * purposes, allowing developers to see which extensions are available for use within the application.
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * app.printExtensions();
     * }
     */
    public void printExtensions() {
        printExtensions(System.out);
    }

    /**
     * Prints the available extensions to a specified {@link PrintStream}. This method offers flexibility in directing
     * the output to different destinations, facilitating integration with logging frameworks or custom output handling.
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * app.printExtensions(System.err);
     * }
     *
     * @param out The PrintStream to print the extensions to.
     */
    public void printExtensions(PrintStream out) {
        out.println("Available Aspire4J Extensions:");
        extensions.stream().sorted(Comparator.comparing(Extension::getName)).forEach(e -> {
            out.println("  - " + e.getName() + " (" + e.getClass().getSimpleName() + ".class): " + e.getDescription());
        });
    }

    /**
     * Loads the specified extension and makes an instance of it available for configuration, but it does not
     * add the extension to the distributed application. This will happen when the extension is configured. This method
     * is key to the extensibility of the Aspire4J framework, allowing developers to dynamically add and configure
     * extensions that enhance the application's functionality.
     * <p>
     * Usage example:
     *
     * {@snippet lang="java" :
     * SpringExtension springExtension = app.withExtension(SpringExtension.class);
     * }
     *
     * @param extension The class of the extension to load.
     * @param <T> The type of the extension.
     * @return An instance of the specified extension class, ready for configuration.
     * @throws RuntimeException If the extension class cannot be instantiated.
     */
    public <T extends Extension> T withExtension(Class<T> extension) {
        try {
            return extension.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create a new instance of the extension class", e);
        }
    }
}
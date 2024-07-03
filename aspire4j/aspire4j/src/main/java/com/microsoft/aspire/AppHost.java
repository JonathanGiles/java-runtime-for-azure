package com.microsoft.aspire;

import java.nio.file.Path;

public interface AppHost {

    /**
     * This is the primary front door through which users interact with the apphost. It can be called directly from
     * the main method of your AppHost implementation. It will parse the runtime arguments and determine the mode of
     * operation, and then proceed to execute the appropriate method.
     *
     * @param args An array of runtime arguments received when the application first starts.
     */
    default void boot(String... args) {
        AppHostBootstrap.boot(this, args);
    }

    /**
     * Call this from the main method of your AppHost implementation to run the apphost locally.
     */
    default void run() {
        throw new RuntimeException("Running the AppHost locally is not supported at present.");
    }

    /**
     * Call this to generate a manifest file for the apphost.
     */
    default void generateManifest(Path outputDir) {
        new ManifestGenerator().generateManifest(this, outputDir);
    }

    /**
     * Called with a new DistributedApplication instance, allowing for the apphost to configure it.
     */
    void configureApplication(DistributedApplication app);
}

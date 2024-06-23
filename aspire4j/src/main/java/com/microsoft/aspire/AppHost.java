package com.microsoft.aspire;

public interface AppHost {
    /**
     * Call this from the main method of your AppHost implementation to run the apphost.
     */
    default void run() {
        new ManifestGenerator().run(this);
    }

    /**
     * Called with a new DistributedApplication instance, allowing for the apphost to configure it.
     */
    void configureApplication(DistributedApplication app);
}

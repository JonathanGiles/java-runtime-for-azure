package com.microsoft.aspire;

public interface AppHost {
    default void run() {
        new ManifestGenerator().run(this);
    }

    void configureApplication(DistributedApplication app);
}

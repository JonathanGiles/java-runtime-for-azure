package com.microsoft.aspire.storageexplorer;

import com.microsoft.aspire.AppHost;
import com.microsoft.aspire.DistributedApplication;

public class StorageExplorerAppHost implements AppHost {

    @Override public void configureApplication(DistributedApplication app) {
        app.printExtensions();
    }

    public static void main(String[] args) {
        new StorageExplorerAppHost().boot(args);
    }
}

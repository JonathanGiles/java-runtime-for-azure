module storage.explorer.apphost {
    requires com.microsoft.aspire;

    requires com.microsoft.aspire.extensions.azure.storage;

    provides com.microsoft.aspire.AppHost with com.microsoft.aspire.springsample.StorageExplorerAppHost;
}
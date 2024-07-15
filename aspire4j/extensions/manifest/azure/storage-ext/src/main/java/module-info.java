
/**
 * An Aspire extension providing support for Azure Storage.
 */
module com.microsoft.aspire.extensions.manifest.azure.storage {
    requires transitive com.microsoft.aspire.extensions.azure.storage;
    requires manifold.ext.rt;

    exports azure.storage.manifest.extensions.com.microsoft.aspire.DistributedApplication;
}
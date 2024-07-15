
/**
 * An Aspire extension providing support for Azure Storage.
 */
module com.microsoft.aspire.extensions.manifold.azure.storage {
    requires transitive com.microsoft.aspire.extensions.azure.storage;
    requires manifold.rt;
    requires manifold.ext.rt;

    exports manifold.azure.storage.extensions.com.microsoft.aspire.DistributedApplication;
}

/**
 * An extension providing support for Azure Storage.
 */
module com.azure.runtime.host.extensions.manifold.azure.storage {
    requires transitive com.azure.runtime.host.extensions.azure.storage;
    requires manifold.rt;
    requires manifold.ext.rt;

    exports manifold.azure.storage.extensions.com.azure.runtime.host.DistributedApplication;
}
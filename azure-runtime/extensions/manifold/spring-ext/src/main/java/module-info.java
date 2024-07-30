
/**
 * An extension providing support for <a href="https://spring.io">Spring</a> projects.
 */
module com.azure.runtime.host.extensions.manifold.spring {
    requires transitive com.azure.runtime.host.extensions.spring;
    requires manifold.rt;
    requires manifold.ext.rt;

    exports manifold.spring.extensions.com.azure.runtime.host.DistributedApplication;
}
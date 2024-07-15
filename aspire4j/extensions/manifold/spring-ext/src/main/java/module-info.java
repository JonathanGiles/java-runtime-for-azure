
/**
 * An Aspire extension providing support for <a href="https://spring.io">Spring</a> projects.
 */
module com.microsoft.aspire.extensions.manifold.spring {
    requires transitive com.microsoft.aspire.extensions.spring;
    requires manifold.rt;
    requires manifold.ext.rt;

    exports manifold.spring.extensions.com.microsoft.aspire.DistributedApplication;
}
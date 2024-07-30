/**
 * An extension providing the common support for microservice libraries (such as Spring, Micronaut, and Quarkus).
 */
module com.azure.runtime.host.extensions.microservice.common {
    requires transitive com.azure.runtime.host;
    requires com.github.javaparser.core;
    requires java.xml;
    requires java.logging;
    requires maven.model;
    requires plexus.utils;

    exports com.azure.runtime.host.extensions.microservice.common;
    exports com.azure.runtime.host.extensions.microservice.common.resources;
    exports com.azure.runtime.host.extensions.microservice.common.utils;

    opens com.azure.runtime.host.extensions.microservice.common.resources to org.hibernate.validator, com.fasterxml.jackson.databind;

    // We conditionally open up the template files to the apphost, so it can write them out
    opens templates.eureka to com.azure.runtime.host;
    opens templates.opentelemetry to com.azure.runtime.host;

}
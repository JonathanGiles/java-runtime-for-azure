/**
 * An Aspire extension providing the common support for microservice libraries (such as Spring, Micronaut, and Quarkus).
 */
module com.microsoft.aspire.extensions.microservice.common {
    requires transitive com.microsoft.aspire;
    requires com.github.javaparser.core;
    requires java.xml;
    requires java.logging;
    requires maven.model;
    requires plexus.utils;

    exports com.microsoft.aspire.extensions.microservice.common;
    exports com.microsoft.aspire.extensions.microservice.common.resources;
    exports com.microsoft.aspire.extensions.microservice.common.utils;

    opens com.microsoft.aspire.extensions.microservice.common.resources to org.hibernate.validator, com.fasterxml.jackson.databind;

    // We conditionally open up the template files to the apphost, so it can write them out
    opens templates.eureka to com.microsoft.aspire;
    opens templates.opentelemetry to com.microsoft.aspire;

}
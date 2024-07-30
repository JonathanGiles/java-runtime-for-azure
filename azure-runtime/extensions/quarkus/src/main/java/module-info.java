import com.azure.runtime.host.Extension;
import com.azure.runtime.host.extensions.quarkus.QuarkusExtension;

/**
 * An extension providing support for <a href="https://quarkus.io">Quarkus</a> projects.
 */
module com.azure.runtime.host.extensions.quarkus {
    requires transitive com.azure.runtime.host.extensions.microservice.common;
    requires java.logging;

    exports com.azure.runtime.host.extensions.quarkus;
    exports com.azure.runtime.host.extensions.quarkus.resources;

    opens com.azure.runtime.host.extensions.quarkus to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.azure.runtime.host.extensions.quarkus.resources to com.fasterxml.jackson.databind, org.hibernate.validator;

    provides Extension with QuarkusExtension;
}
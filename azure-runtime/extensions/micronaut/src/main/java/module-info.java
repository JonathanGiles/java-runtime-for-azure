import com.azure.runtime.host.Extension;
import com.azure.runtime.host.extensions.micronaut.MicronautExtension;

/**
 * An extension providing support for <a href="https://micronaut.io">Micronaut</a> projects.
 */
module com.azure.runtime.host.extensions.micronaut {
    requires transitive com.azure.runtime.host.extensions.microservice.common;
    requires java.logging;

    exports com.azure.runtime.host.extensions.micronaut;
    exports com.azure.runtime.host.extensions.micronaut.resources;

    opens com.azure.runtime.host.extensions.micronaut to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.azure.runtime.host.extensions.micronaut.resources to com.fasterxml.jackson.databind, org.hibernate.validator;

    provides Extension with MicronautExtension;
}
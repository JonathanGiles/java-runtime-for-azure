import com.microsoft.aspire.extensions.micronaut.MicronautExtension;

/**
 * An Aspire extension providing support for <a href="https://micronaut.io">Micronaut</a> projects.
 */
module com.microsoft.aspire.extensions.micronaut {
    requires transitive com.microsoft.aspire.extensions.microservice.common;
    requires java.logging;

    exports com.microsoft.aspire.extensions.micronaut;
    exports com.microsoft.aspire.extensions.micronaut.resources;

    opens com.microsoft.aspire.extensions.micronaut to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.microsoft.aspire.extensions.micronaut.resources to com.fasterxml.jackson.databind, org.hibernate.validator;

    provides com.microsoft.aspire.Extension with MicronautExtension;
}
import com.microsoft.aspire.extensions.quarkus.QuarkusExtension;

module com.microsoft.aspire.extensions.micronaut {
    requires transitive com.microsoft.aspire.extensions.microservice.common;
    requires java.logging;

    exports com.microsoft.aspire.extensions.quarkus;
    exports com.microsoft.aspire.extensions.quarkus.resources;

    opens com.microsoft.aspire.extensions.quarkus to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.microsoft.aspire.extensions.quarkus.resources to com.fasterxml.jackson.databind, org.hibernate.validator;

    provides com.microsoft.aspire.Extension with QuarkusExtension;
}
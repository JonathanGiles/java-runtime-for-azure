import com.azure.runtime.host.Extension;

/**
 * The core module of the Java Runtime for Azure framework, with APIs for specifying and creating resources on Azure.
 */
module com.azure.runtime.host {
    requires transitive com.fasterxml.jackson.databind;
    requires transitive jakarta.validation;
    requires org.hibernate.validator;
    requires java.logging;
    requires freemarker;

    exports com.azure.runtime.host;
    exports com.azure.runtime.host.resources;
    exports com.azure.runtime.host.resources.annotations;
    exports com.azure.runtime.host.resources.properties;
    exports com.azure.runtime.host.resources.references;
    exports com.azure.runtime.host.resources.traits;
    exports com.azure.runtime.host.utils;
    exports com.azure.runtime.host.utils.json;
    exports com.azure.runtime.host.utils.templates;

    opens com.azure.runtime.host.implementation.utils.json to com.fasterxml.jackson.databind;

    opens com.azure.runtime.host to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.azure.runtime.host.resources to org.hibernate.validator, com.fasterxml.jackson.databind;

    uses Extension;
}
module com.microsoft.aspire {
    requires transitive com.fasterxml.jackson.databind;
    requires transitive jakarta.validation;
    requires transitive org.hibernate.validator;
    requires java.logging;

    // Templating engines
//    requires velocity.engine.core;
    requires freemarker;

    exports com.microsoft.aspire;
    exports com.microsoft.aspire.resources;
    exports com.microsoft.aspire.resources.properties;
    exports com.microsoft.aspire.resources.traits;
    exports com.microsoft.aspire.utils.templates;

    opens com.microsoft.aspire.resources to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.microsoft.aspire.resources.properties to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.microsoft.aspire to com.fasterxml.jackson.databind, org.hibernate.validator;
    opens com.microsoft.aspire.implementation.json to com.fasterxml.jackson.databind;
    opens com.microsoft.aspire.implementation to com.fasterxml.jackson.databind;

    uses com.microsoft.aspire.Extension;
}
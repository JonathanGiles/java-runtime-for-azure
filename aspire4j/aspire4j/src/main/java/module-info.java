module com.microsoft.aspire {
    requires transitive com.fasterxml.jackson.databind;
    requires transitive jakarta.validation;
    requires transitive org.hibernate.validator;
    requires java.logging;
    requires freemarker;

    exports com.microsoft.aspire;
    exports com.microsoft.aspire.resources;
    exports com.microsoft.aspire.resources.annotations;
    exports com.microsoft.aspire.resources.properties;
    exports com.microsoft.aspire.resources.references;
    exports com.microsoft.aspire.resources.traits;
    exports com.microsoft.aspire.utils;
    exports com.microsoft.aspire.utils.json;
    exports com.microsoft.aspire.utils.templates;

    opens com.microsoft.aspire.resources to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.microsoft.aspire.resources.properties to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.microsoft.aspire to com.fasterxml.jackson.databind, org.hibernate.validator;
    opens com.microsoft.aspire.implementation to com.fasterxml.jackson.databind;
    opens com.microsoft.aspire.utils to com.fasterxml.jackson.databind;
    opens com.microsoft.aspire.resources.annotations to com.fasterxml.jackson.databind, org.hibernate.validator;
    opens com.microsoft.aspire.resources.references to com.fasterxml.jackson.databind, org.hibernate.validator;
    opens com.microsoft.aspire.utils.json to com.fasterxml.jackson.databind;


    uses com.microsoft.aspire.Extension;
}
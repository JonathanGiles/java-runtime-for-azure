module com.microsoft.aspire {
    requires com.fasterxml.jackson.databind;
    requires jakarta.validation;
    requires org.hibernate.validator;

    exports com.microsoft.aspire;
    exports com.microsoft.aspire.components.azure;
    exports com.microsoft.aspire.components.common;
    exports com.microsoft.aspire.components.common.properties;
    exports com.microsoft.aspire.components.common.traits;

    opens com.microsoft.aspire.implementation.manifest to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.microsoft.aspire.components.azure to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.microsoft.aspire.components.common to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.microsoft.aspire.components.common.properties to org.hibernate.validator, com.fasterxml.jackson.databind;

    uses com.microsoft.aspire.AppHost;
}
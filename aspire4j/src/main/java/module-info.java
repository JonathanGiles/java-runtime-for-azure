module com.microsoft.aspire {
    requires com.fasterxml.jackson.databind;
    requires jakarta.validation;
    requires org.hibernate.validator;

    exports com.microsoft.aspire;
    exports com.microsoft.aspire.components.azure;
    exports com.microsoft.aspire.components.common;
    exports com.microsoft.aspire.components.common.properties;
    exports com.microsoft.aspire.components.common.traits;
}
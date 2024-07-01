import com.microsoft.aspire.extensions.spring.SpringExtension;

module com.microsoft.aspire.extensions.spring {
    requires transitive com.microsoft.aspire;
    requires com.github.javaparser.core;
    requires java.xml;
    requires java.logging;

    exports com.microsoft.aspire.extensions.spring;

    opens com.microsoft.aspire.extensions.spring to org.hibernate.validator, com.fasterxml.jackson.databind;
    exports com.microsoft.aspire.extensions.spring.resources;
    opens com.microsoft.aspire.extensions.spring.resources to com.fasterxml.jackson.databind, org.hibernate.validator;

    provides com.microsoft.aspire.Extension with SpringExtension;
}
import com.microsoft.aspire.extensions.spring.SpringExtension;

module com.microsoft.aspire.extensions.spring {
    requires transitive com.microsoft.aspire;
    requires com.github.javaparser.core;
    requires java.xml;
    requires java.logging;
    requires maven.model;

    exports com.microsoft.aspire.extensions.spring;
    exports com.microsoft.aspire.extensions.spring.resources;
    opens com.microsoft.aspire.extensions.spring to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.microsoft.aspire.extensions.spring.implementation to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.microsoft.aspire.extensions.spring.resources to com.fasterxml.jackson.databind, org.hibernate.validator;

    // We conditionally open up the template files to the apphost, so it can write them out
    opens templates.eureka to com.microsoft.aspire;

    provides com.microsoft.aspire.Extension with SpringExtension;
}
import com.azure.runtime.host.Extension;
import com.azure.runtime.host.extensions.spring.SpringExtension;

/**
 * An extension providing support for <a href="https://spring.io">Spring</a> projects.
 */
module com.azure.runtime.host.extensions.spring {
    requires transitive com.azure.runtime.host.extensions.microservice.common;

    exports com.azure.runtime.host.extensions.spring;
    exports com.azure.runtime.host.extensions.spring.resources;

    opens com.azure.runtime.host.extensions.spring to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.azure.runtime.host.extensions.spring.resources to com.fasterxml.jackson.databind, org.hibernate.validator;

    provides Extension with SpringExtension;
}
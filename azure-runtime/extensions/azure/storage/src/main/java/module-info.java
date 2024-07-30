import com.azure.runtime.host.Extension;
import com.azure.runtime.host.extensions.azure.storage.AzureStorageExtension;

/**
 * An extension providing support for Azure Storage.
 */
module com.azure.runtime.host.extensions.azure.storage {
    requires transitive com.azure.runtime.host;

    exports com.azure.runtime.host.extensions.azure.storage;
    exports com.azure.runtime.host.extensions.azure.storage.resources;

    opens com.azure.runtime.host.extensions.azure.storage to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.azure.runtime.host.extensions.azure.storage.resources to com.fasterxml.jackson.databind, org.hibernate.validator;

    // We conditionally open up the template files to the apphost, so it can write them out
    opens templates.bicep to com.azure.runtime.host;

    provides Extension with AzureStorageExtension;
}
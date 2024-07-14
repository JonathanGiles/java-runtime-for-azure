import com.microsoft.aspire.extensions.azure.storage.AzureStorageExtension;

module com.microsoft.aspire.extensions.azure.storage {
    requires transitive com.microsoft.aspire;

    exports com.microsoft.aspire.extensions.azure.storage;
    exports com.microsoft.aspire.extensions.azure.storage.resources;

    opens com.microsoft.aspire.extensions.azure.storage to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.microsoft.aspire.extensions.azure.storage.resources to com.fasterxml.jackson.databind, org.hibernate.validator;

    // We conditionally open up the template files to the apphost, so it can write them out
    opens templates.bicep to com.microsoft.aspire;

    provides com.microsoft.aspire.Extension with AzureStorageExtension;
}
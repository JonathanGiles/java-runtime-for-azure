import com.microsoft.aspire.extensions.azure.storage.AzureStorageExtension;

module com.microsoft.aspire.extensions.azure.storage {
    requires transitive com.microsoft.aspire;

    exports com.microsoft.aspire.extensions.azure.storage;

    opens com.microsoft.aspire.extensions.azure.storage to org.hibernate.validator, com.fasterxml.jackson.databind;

    provides com.microsoft.aspire.Extension with AzureStorageExtension;
}
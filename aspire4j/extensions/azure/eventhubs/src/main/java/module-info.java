import com.microsoft.aspire.extensions.azure.eventhubs.AzureEventHubsExtension;

module com.microsoft.aspire.extensions.azure.eventhubs {
    requires transitive com.microsoft.aspire;

    exports com.microsoft.aspire.extensions.azure.eventhubs;
    exports com.microsoft.aspire.extensions.azure.eventhubs.resources;

    opens com.microsoft.aspire.extensions.azure.eventhubs to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.microsoft.aspire.extensions.azure.eventhubs.resources to com.fasterxml.jackson.databind, org.hibernate.validator;

    // We conditionally open up the template files to the apphost, so it can write them out
    opens templates.bicep to com.microsoft.aspire;

    provides com.microsoft.aspire.Extension with AzureEventHubsExtension;
}
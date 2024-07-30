import com.azure.runtime.host.Extension;
import com.azure.runtime.host.extensions.azure.eventhubs.AzureEventHubsExtension;

/**
 * An extension providing support for Azure Event Hubs.
 */
module com.azure.runtime.host.extensions.azure.eventhubs {
    requires transitive com.azure.runtime.host;

    exports com.azure.runtime.host.extensions.azure.eventhubs;
    exports com.azure.runtime.host.extensions.azure.eventhubs.resources;

    opens com.azure.runtime.host.extensions.azure.eventhubs to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.azure.runtime.host.extensions.azure.eventhubs.resources to com.fasterxml.jackson.databind, org.hibernate.validator;

    // We conditionally open up the template files to the apphost, so it can write them out
    opens templates.bicep to com.azure.runtime.host;

    provides Extension with AzureEventHubsExtension;
}
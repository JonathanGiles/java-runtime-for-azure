import com.azure.runtime.host.Extension;
import com.azure.runtime.host.extensions.azure.openai.AzureOpenAIExtension;

/**
 * An extension providing support for Azure OpenAI.
 */
module com.azure.runtime.host.extensions.azure.openai {
    requires transitive com.azure.runtime.host;

    exports com.azure.runtime.host.extensions.azure.openai;
    exports com.azure.runtime.host.extensions.azure.openai.resources;

    opens com.azure.runtime.host.extensions.azure.openai to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.azure.runtime.host.extensions.azure.openai.resources to com.fasterxml.jackson.databind, org.hibernate.validator;

    // We conditionally open up the template files to the apphost, so it can write them out
    opens templates.openai.bicep to com.azure.runtime.host;

    provides Extension with AzureOpenAIExtension;
}
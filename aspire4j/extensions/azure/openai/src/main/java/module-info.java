import com.microsoft.aspire.extensions.azure.openai.AzureOpenAIExtension;

/**
 * An Aspire extension providing support for Azure OpenAI.
 */
module com.microsoft.aspire.extensions.azure.openai {
    requires transitive com.microsoft.aspire;

    exports com.microsoft.aspire.extensions.azure.openai;
    exports com.microsoft.aspire.extensions.azure.openai.resources;

    opens com.microsoft.aspire.extensions.azure.openai to org.hibernate.validator, com.fasterxml.jackson.databind;
    opens com.microsoft.aspire.extensions.azure.openai.resources to com.fasterxml.jackson.databind, org.hibernate.validator;

    // We conditionally open up the template files to the apphost, so it can write them out
    opens templates.openai.bicep to com.microsoft.aspire;

    provides com.microsoft.aspire.Extension with AzureOpenAIExtension;
}
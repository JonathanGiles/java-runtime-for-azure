package com.azure.runtime.host;

/**
 * An extension is a collection of resources that can be used to extend the functionality of the AppHost API.
 */
public interface Extension {
    /**
     * Returns the extension name - which is used when displaying the extension in the UI / CLI.
     *
     * @return The name of the extension.
     */
    String getName();

    /**
     * Returns a description of the extension - which is used when displaying the extension in the UI / CLI.
     *
     * @return The description of the extension.
     */
    String getDescription();
}

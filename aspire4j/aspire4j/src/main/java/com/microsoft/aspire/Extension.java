package com.microsoft.aspire;

import com.microsoft.aspire.resources.Resource;

import java.util.List;

/**
 * An extension is a collection of resources that can be used to extend the functionality of the Aspire AppHost API.
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

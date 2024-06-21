package com.microsoft.aspire.components.common.traits;

public interface ResourceWithConnectionString<T extends ResourceWithConnectionString<T>> {

//    T withConnectionString(String connectionString);

    /**
     * Gets the connection string associated with the resource.
     * @return
     */
    String getConnectionString();

    /**
     * The environment variable name to use for the connection string.
     * @return
     */
    String getConnectionStringEnvironmentVariable();
}

package com.microsoft.aspire.resources.traits;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ResourceWithConnectionString<T extends ResourceWithConnectionString<T>> {

//    T withConnectionString(String connectionString);

    /**
     * Gets the connection string associated with the resource.
     * @return
     */
    String getConnectionString();

//    /**
//     * The environment variable name to use for the connection string.
//     * @return
//     */
//    String getConnectionStringEnvironmentVariable();

    /**
     * An override of the source resource's name for the connection string. The resulting connection string will be
     * "ConnectionStrings__connectionName" if this is not null.
     */
    @JsonIgnore
    default String getConnectionName() {
        return null;
    }
}

package com.azure.runtime.host.resources.annotations;

import java.util.Collections;
import java.util.Map;

/**
 * Represents a callback context for environment variables associated with a publisher.
 */
public class EnvironmentCallbackContext {
    private final Map<String, Object> environmentVariables;
//    private final DistributedApplicationExecutionContext executionContext;

    /**
     * Initializes a new instance of the EnvironmentCallbackContext class.
     *
     * @param environmentVariables The environment variables associated with this execution. It's optional and defaults to an empty map.
     */
    public EnvironmentCallbackContext(Map<String, Object> environmentVariables) {
//        if (executionContext == null) {
//            throw new IllegalArgumentException("executionContext cannot be null");
//        }
//        this.executionContext = executionContext;
        this.environmentVariables = environmentVariables != null ? environmentVariables : Collections.emptyMap();
    }

    /**
     * Gets the environment variables associated with the callback context.
     *
     * @return The environment variables.
     */
    public Map<String, Object> getEnvironmentVariables() {
        return environmentVariables;
    }
//
//    /**
//     * Gets or sets an optional logger to use for logging.
//     *
//     * @return The logger.
//     */
//    public Logger getLogger() {
//        return logger;
//    }
//
//    public void setLogger(Logger logger) {
//        this.logger = logger;
//    }
//
//    /**
//     * Gets the execution context associated with this invocation of the AppHost.
//     *
//     * @return The execution context.
//     */
//    public DistributedApplicationExecutionContext getExecutionContext() {
//        return executionContext;
//    }
}
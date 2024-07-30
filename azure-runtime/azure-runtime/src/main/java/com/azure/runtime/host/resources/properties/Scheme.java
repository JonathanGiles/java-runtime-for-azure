package com.azure.runtime.host.resources.properties;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The network scheme: HTTP, HTTPS, TCP, or UDP.
 */
public enum Scheme {
    /**
     * HTTP scheme.
     */
    HTTP("http"),

    /**
     * HTTPS scheme.
     */
    HTTPS("https"),

    /**
     * TCP scheme.
     */
    TCP("tcp"),

    /**
     * UDP scheme.
     */
    UDP("udp");

    private final String value;

    Scheme(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}

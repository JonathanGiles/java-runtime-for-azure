package com.azure.runtime.host.resources.properties;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Transport that is being used (e.g. http, http2, http3 etc).
 */
public enum Transport {
    /**
     * HTTP transport.
     */
    HTTP("http"),

    /**
     * HTTP2 transport.
     */
    HTTP2("http2"),

    /**
     * TCP transport.
     */
    TCP("tcp");

    private final String value;

    Transport(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}

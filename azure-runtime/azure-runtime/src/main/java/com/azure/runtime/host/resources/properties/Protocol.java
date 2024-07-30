package com.azure.runtime.host.resources.properties;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Network protocol: TCP or UDP are supported today, others possibly in future.
 */
public enum Protocol {
    /**
     * Transmission Control Protocol.
     */
    TCP("tcp"),

    /**
     * User Datagram Protocol.
     */
    UDP("udp");

    private final String value;

    Protocol(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}

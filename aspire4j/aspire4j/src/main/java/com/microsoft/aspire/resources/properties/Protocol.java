package com.microsoft.aspire.resources.properties;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Protocol {
    TCP("tcp"),
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

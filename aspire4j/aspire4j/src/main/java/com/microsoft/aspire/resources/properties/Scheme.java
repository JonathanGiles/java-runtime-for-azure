package com.microsoft.aspire.resources.properties;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Scheme {
    HTTP("http"),
    HTTPS("https"),
    TCP("tcp"),
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

package com.microsoft.aspire.resources.properties;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Transport {
    HTTP("http"),
    HTTP2("http2"),
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

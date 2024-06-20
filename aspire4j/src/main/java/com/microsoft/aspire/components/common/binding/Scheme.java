package com.microsoft.aspire.components.common.binding;

public enum Scheme {
    HTTP("http"),
    HTTPS("https"),
    TCP("tcp"),
    UDP("udp");

    private final String value;

    Scheme(String value) {
        this.value = value;
    }
}

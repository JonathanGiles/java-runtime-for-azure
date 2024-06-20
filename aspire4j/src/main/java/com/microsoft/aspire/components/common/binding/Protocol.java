package com.microsoft.aspire.components.common.binding;

public enum Protocol {
    TCP("tcp"),
    UDP("udp");

    private final String value;

    Protocol(String value) {
        this.value = value;
    }
}

package com.microsoft.aspire.components.common.binding;

public enum Transport {
    HTTP("http"),
    HTTP2("http2"),
    TCP("tcp");

    private final String value;

    Transport(String value) {
        this.value = value;
    }
}

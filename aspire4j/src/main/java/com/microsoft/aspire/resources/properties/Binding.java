package com.microsoft.aspire.resources.properties;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/*
 "additionalProperties": {
    "type": "object",
    "required": [
        "scheme",
        "protocol",
        "transport"
    ],
    "properties": {
        "scheme": {
            "type": "string",
            "description": "The scheme used in URIs for this binding.",
            "enum": [
                "http",
                "https",
                "tcp",
                "udp"
            ]
        },
        "protocol": {
            "type": "string",
            "description": "The protocol used for this binding (only 'tcp' or 'udp' are valid).",
            "enum": [
                "tcp",
                "udp"
            ]
        },
        "transport": {
            "type": "string",
            "description": "Additional information describing the transport (e.g. HTTP/2).",
            "enum": [
                "http",
                "http2",
                "tcp"
            ]
        },
        "external": {
            "type": "boolean",
            "description": "A flag indicating whether this binding is exposed externally when deployed."
        },
        "targetPort": {
            "type": "number",
            "description": "The port that the workload (e.g. container) is listening on."
        },
        "port": {
            "type": "number",
            "description": "The port that the workload (e.g. container) is exposed as to other resources and externally."
        }
    },
    "additionalProperties": false
}
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Binding {

    public enum Protocol {
        TCP("tcp"),
        UDP("udp");

        private final String value;

        Protocol(String value) {
            this.value = value;
        }

        @JsonValue
        public String toString() {
            return value;
        }
    }

    public enum Scheme {
        HTTP("http"),
        HTTPS("https"),
        TCP("tcp"),
        UDP("udp");

        private final String value;

        Scheme(String value) {
            this.value = value;
        }

        @JsonValue
        public String toString() {
            return value;
        }
    }

    public enum Transport {
        HTTP("http"),
        HTTP2("http2"),
        TCP("tcp");

        private final String value;

        Transport(String value) {
            this.value = value;
        }

        @JsonValue
        public String toString() {
            return value;
        }
    }

    @NotNull(message = "Binding.scheme cannot be null")
    @JsonProperty("scheme")
    private final Scheme scheme;

    @NotNull(message = "Binding.protocol cannot be null")
    @JsonProperty("protocol")
    private final Protocol protocol;

    @NotNull(message = "Binding.transport cannot be null")
    @JsonProperty("transport")
    private final Transport transport;

    @JsonProperty("external")
    private Boolean external;

    @JsonProperty("targetPort")
    @Min(value = 0, message = "Binding.targetPort must be between 0 and 65535")
    @Max(value = 65535, message = "Binding.targetPort must be between 0 and 65535")
    private Integer targetPort;

    @JsonProperty("port")
    @Min(value = 0, message = "Binding.port must be between 0 and 65535")
    @Max(value = 65535, message = "Binding.port must be between 0 and 65535")
    private Integer port;

    public Binding(Scheme scheme, Protocol protocol, Transport transport) {
        this.scheme = scheme;
        this.protocol = protocol;
        this.transport = transport;
    }

    public Binding withExternal() {
        this.external = true;
        return this;
    }

    public Binding withTargetPort(Integer targetPort) {
        this.targetPort = targetPort;
        return this;
    }

    public Binding withPort(Integer port) {
        this.port = port;
        return this;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public Transport getTransport() {
        return transport;
    }

    public Boolean getExternal() {
        return external;
    }

    public Integer getTargetPort() {
        return targetPort;
    }

    public Integer getPort() {
        return port;
    }
}
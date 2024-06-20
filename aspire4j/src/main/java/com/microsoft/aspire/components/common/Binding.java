package com.microsoft.aspire.components.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.aspire.components.common.binding.Protocol;
import com.microsoft.aspire.components.common.binding.Scheme;
import com.microsoft.aspire.components.common.binding.Transport;

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

    @JsonProperty("scheme")
    private final Scheme scheme;

    @JsonProperty("protocol")
    private final Protocol protocol;

    @JsonProperty("transport")
    private final Transport transport;

    @JsonProperty("external")
    private Boolean external;

    @JsonProperty("targetPort")
    private Integer targetPort;

    @JsonProperty("port")
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
}
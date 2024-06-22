package com.microsoft.aspire.resources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class BindMount {
    @NotNull(message = "BindMount.source cannot be null")
    @NotEmpty(message = "BindMount.source cannot be an empty string")
    @JsonProperty("source")
    private final String source;

    @NotNull(message = "BindMount.target cannot be null")
    @NotEmpty(message = "BindMount.target cannot be an empty string")
    @JsonProperty("target")
    private final String target;

    @JsonProperty("readonly")
    private final boolean readonly;

    public BindMount(String source, String target, boolean readonly) {
        this.source = source;
        this.target = target;
        this.readonly = readonly;
    }
}

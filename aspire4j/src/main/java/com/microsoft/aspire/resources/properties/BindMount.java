package com.microsoft.aspire.resources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class BindMount {
    @NotNull(message = "BindMount.source cannot be null")
    @NotEmpty(message = "BindMount.source cannot be an empty string")
    @JsonProperty("source")
    private String source;

    @NotNull(message = "BindMount.target cannot be null")
    @NotEmpty(message = "BindMount.target cannot be an empty string")
    @JsonProperty("target")
    private String target;

    @JsonProperty("readonly")
    private boolean readonly;

    public BindMount() { }

    public BindMount(String source, String target, boolean readonly) {
        this.source = source;
        this.target = target;
        this.readonly = readonly;
    }

    public BindMount withSource(String source) {
        this.source = source;
        return this;
    }

    public BindMount withTarget(String target) {
        this.target = target;
        return this;
    }

    public BindMount withReadonly() {
        this.readonly = true;
        return this;
    }
}

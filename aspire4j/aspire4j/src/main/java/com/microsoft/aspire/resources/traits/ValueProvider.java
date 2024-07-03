package com.microsoft.aspire.resources.traits;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ValueProvider {

    @JsonIgnore
    String getValue();
}

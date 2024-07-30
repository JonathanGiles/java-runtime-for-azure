package com.azure.runtime.host.resources.traits;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ValueProvider {

    @JsonIgnore
    String getValue();
}

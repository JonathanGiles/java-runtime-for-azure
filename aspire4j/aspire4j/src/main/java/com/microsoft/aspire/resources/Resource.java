package com.microsoft.aspire.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.aspire.resources.traits.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonPropertyOrder({"type", "params"})
public abstract class Resource<T extends Resource<T>> {

    @Valid
    @NotNull(message = "Resource Type cannot be null")
    @JsonProperty("type")
    private final ResourceType type;

    // the name of the resource
    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be an empty string")
    @JsonIgnore
    private final String name;

    public Resource(ResourceType type, String name) {
        this.type = type;
        this.name = name;
    }

    public final ResourceType getType() {
        return type;
    }

    public final String getName() {
        return name;
    }

    public <T extends Resource> T copyInto(T newResource) {
        // TODO this is incomplete
        // look at the traits of this resource, and copy them into the new resource
        if (this instanceof ResourceWithArguments<?> oldResource && newResource instanceof ResourceWithArguments<?> _newResource) {
            oldResource.getArguments().forEach(_newResource::withArgument);
        }
//        if (this instanceof ResourceWithEndpoints<?> oldResource && newResource instanceof ResourceWithEndpoints<?> _newResource) {
//            rwe.getEndpoints().forEach(nrwe::withEndpoint);
//        }
        if (this instanceof ResourceWithBindings<?> oldResource && newResource instanceof ResourceWithBindings<?> _newResource) {
            oldResource.getBindings().values().forEach(_newResource::withBinding);
        }
//        if (this instanceof ResourceWithConnectionString<?> oldResource && newResource instanceof ResourceWithConnectionString<?> _newResource) {
//            oldResource.getBindings().values().forEach(_newResource::withBinding);
//        }
        if (this instanceof ResourceWithParameters<?> oldResource && newResource instanceof ResourceWithParameters<?> _newResource) {
            oldResource.getParameters().forEach(_newResource::withParameter);
        }
        if (this instanceof ResourceWithEnvironment<?> oldResource && newResource instanceof ResourceWithEnvironment<?> _newResource) {
            oldResource.getEnvironment().forEach(_newResource::withEnvironment);
        }
        return newResource;
    }

    // This method is used to return "this" as the correct type
    protected abstract T self();
}

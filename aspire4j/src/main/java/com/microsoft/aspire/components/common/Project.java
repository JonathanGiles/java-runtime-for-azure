package com.microsoft.aspire.components.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.aspire.components.common.traits.ResourceWithArguments;
import com.microsoft.aspire.components.common.traits.ResourceWithEnvironment;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Project extends Resource implements ResourceWithEnvironment<Project> {

    @JsonProperty("mavenPath")
    private String mavenPath;

    // FIXME this isn't just a true / false that is written out
    @JsonProperty
    private boolean withExternalHttpEndpoints;

    @JsonProperty("env")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> env = new LinkedHashMap<>();

    public Project(String name) {
        super("java.project.v0", name);
    }

    /**
     * The path to the project file. Relative paths are interpreted as being relative to the location of the manifest file.
     * @param mavenPath
     * @return
     */
    public Project withMavenPath(String mavenPath) {
        this.mavenPath = mavenPath;
        return this;
    }

    public Project withEnvironment(String key, String value) {
        this.env.put(key, value);
        return this;
    }

    public Project withExternalHttpEndpoints() {
        this.withExternalHttpEndpoints = true;
        return this;
    }

    public Project withReference(Resource resource) {
        // TODO
        // https://learn.microsoft.com/en-us/dotnet/api/aspire.hosting.resourcebuilderextensions.withreference?view=dotnet-aspire-8.0.1#aspire-hosting-resourcebuilderextensions-withreference-1(aspire-hosting-applicationmodel-iresourcebuilder((-0))-aspire-hosting-applicationmodel-iresourcebuilder((aspire-hosting-applicationmodel-iresourcewithconnectionstring))-system-string-system-boolean)
        return this;
    }
}

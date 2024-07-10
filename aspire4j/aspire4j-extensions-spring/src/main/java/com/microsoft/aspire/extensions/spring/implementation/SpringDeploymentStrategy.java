package com.microsoft.aspire.extensions.spring.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpringDeploymentStrategy implements Comparable<SpringDeploymentStrategy> {

    public enum DeploymentType {
        DOCKER_FILE,
        DOCKER_COMPOSE,
        MAVEN_POM,
        GRADLE_BUILD;
    }

    private final DeploymentType type;
    private final List<String[]> commands;
    private final int priority;

    /**
     *
     * @param type
     * @param priority The higher the priority, the higher it goes on the resulting list of possible strategies
     */
    public SpringDeploymentStrategy(DeploymentType type, int priority) {
        this.type = type;
        this.priority = priority;
        this.commands = new ArrayList<>();
    }

    public DeploymentType getType() {
        return type;
    }

    public SpringDeploymentStrategy withCommand(String[] command) {
        commands.add(command);
        return this;
    }

    public List<String[]> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    @Override
    public int compareTo(SpringDeploymentStrategy o) {
        // Higher priority comes first
        return Integer.compare(priority, o.priority);
    }
}
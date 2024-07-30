package com.azure.runtime.host.resources.references;

import com.azure.runtime.host.resources.traits.ValueProvider;
import com.azure.runtime.host.resources.traits.ValueWithReferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an expression that might be made up of multiple resource properties. For example, a connection string
 * might be made up of a host, port, and password from different endpoints.
 */
public class ReferenceExpression implements ValueProvider, ValueWithReferences {
    private final String format;
    private final List<ValueProvider> valueProviders;
    private final List<String> manifestExpressions;

    /**
     * Constructs a new ReferenceExpression.
     *
     * @param format             The format string for this expression.
     * @param valueProviders     The list of ValueProvider that will be used to resolve parameters for the format string.
     * @param manifestExpressions The manifest expressions for the parameters for the format string.
     */
    private ReferenceExpression(String format, List<ValueProvider> valueProviders, List<String> manifestExpressions) {
        if (format == null || valueProviders == null || manifestExpressions == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        this.format = format;
        this.valueProviders = new ArrayList<>(valueProviders);
        this.manifestExpressions = List.copyOf(manifestExpressions);
    }

    /**
     * Gets the format string for this expression.
     *
     * @return The format string.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Gets the manifest expressions for the parameters for the format string.
     *
     * @return The manifest expressions.
     */
    public List<String> getManifestExpressions() {
        return manifestExpressions;
    }

    /**
     * Gets the list of ValueProvider that will be used to resolve parameters for the format string.
     *
     * @return The list of ValueProvider.
     */
    public List<ValueProvider> getValueProviders() {
        return valueProviders;
    }

    @Override
    public List<Object> getReferences() {
        return List.of(valueProviders);
    }

    /**
     * The value expression for the format string.
     *
     * @return The value expression.
     */
    public String getValueExpression() {
        return String.format(format, manifestExpressions.toArray());
    }

    /**
     * Gets the value of the expression. The final string value after evaluating the format string and its parameters.
     *
     * @return A CompletionStage containing the evaluated string.
     */
    @Override
    public String getValue() {
        String[] values = valueProviders.stream()
            .map(ValueProvider::getValue)
            .toArray(String[]::new);
        return String.format(format, (Object) values);
    }

    // TODO this is not complete!
    public static ReferenceExpression create(String format) {
        return new ReferenceExpression(format, new ArrayList<>(), new ArrayList<>());
    }
}
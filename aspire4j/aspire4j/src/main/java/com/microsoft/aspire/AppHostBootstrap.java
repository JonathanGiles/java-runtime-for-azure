package com.microsoft.aspire;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

// Not public API
class AppHostBootstrap {

    static {
        try (InputStream is = AppHostBootstrap.class.getClassLoader().getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static final Logger LOGGER = Logger.getLogger(AppHostBootstrap.class.getName());

    private static final String PROPERTY_OUTPUT_DIR = "--output-dir";
    private static final String PROPERTY_MODE = "--mode";

    static class Parameter{
        final List<String> names;
        final String description;
        final String defaultValue;

        Parameter(String name, String description, String defaultValue) {
            this.names = List.of(name);
            this.description = description;
            this.defaultValue = defaultValue;
        }

        Parameter(List<String> names, String description, String defaultValue) {
            this.names = names;
            this.description = description;
            this.defaultValue = defaultValue;
        }

        public List<String> getNames() {
            return names;
        }

        String getDescription() {
            return description;
        }

        String getDefaultValue() {
            return defaultValue;
        }

        @Override
        public String toString() {
            // just return the first name
            return getNames().getFirst();
        }
    }

    private static final List<Parameter> PARAMETERS = new ArrayList<>();

    static {
        PARAMETERS.add(new Parameter(List.of(PROPERTY_OUTPUT_DIR, "-o"), "Set the output directory for generated configuration files such as the Aspire Manifest", "output"));
        PARAMETERS.add(new Parameter(List.of(PROPERTY_MODE, "-m"), "Specifies if the execution mode is 'local' or 'publish'", "publish"));
    }

    public static void boot(AppHost appHost, String[] args) {
        Map<String, String> parsedParameters = new HashMap<>();

        // Initialize parsedParameters with default values
        for (Parameter param : PARAMETERS) {
            parsedParameters.put(param.toString(), param.getDefaultValue());
        }

        // process supplied parameters
        for (String arg : args) {
            if (arg.equals("--help") || arg.equals("-h")) {
                printHelp();
                System.exit(0);
            } else {
                for (Parameter param : PARAMETERS) {
                    for (String name : param.getNames()) {
                        if (arg.startsWith(name + "=")) {
                            String value = arg.substring((name + "=").length());
                            parsedParameters.put(param.toString(), value);
                        }
                    }
                }
            }
        }

        // Use parameters here to bootstrap the app appropriatel here...
        LOGGER.fine("Parsed runtime parameters: " + parsedParameters);

        if (parsedParameters.get(PROPERTY_MODE).equals("local")) {
            appHost.run();
        } else {
            // outputDir is the root path and the specified directory combined
            final Path outputDir = Paths.get(parsedParameters.get(PROPERTY_OUTPUT_DIR)).toAbsolutePath();
            appHost.generateManifest(outputDir);
        }
    }

    private static void printHelp() {
        System.out.println("Aspire4J runtime flags:");
        for (Parameter param : PARAMETERS) {
            String names = String.join(", ", param.getNames());
            System.out.println(names + ": " + param.getDescription() + " (default: " + param.getDefaultValue() + ")");
        }
    }
}

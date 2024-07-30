package com.azure.runtime.host.extensions.microservice.common.utils;

import com.azure.runtime.host.extensions.microservice.common.resources.MicroserviceProject;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BuildIntrospector {
    private static final Logger LOGGER = Logger.getLogger(BuildIntrospector.class.getName());

    private final Set<DeploymentStrategy> strategies = new TreeSet<>();
    private String DEFAULT_SERVER_PORT = "8081";

    public BuildIntrospector() { }

    public Set<DeploymentStrategy> introspect(MicroserviceProject project, Map<String, String> outputEnvs) {
        LOGGER.info("Beginning introspection of project: " + project.getName());

        /*
         * This is where we can look at the project and do things like:
         * - Look for a Dockerfile
         * - Look for a pom.xml or build.gradle file for any known configuration or dependencies
         * - Look for configuration files in known locations
         * - Parse Java source code to look for Spring annotations
         */
        Map<String, String> introspectionProperties = new LinkedHashMap<>();

        lookForBuildFiles(project, introspectionProperties, outputEnvs);
        introspectJavaFiles(project);

        // TODO Take the introspection properties and turn it into a set of useful properties to pass to the aspire-manifest,
        // and modify the properties of the SpringProject directly
//        Map<String, String> properties = new LinkedHashMap<>();

        return strategies;
    }

    private void lookForBuildFiles(MicroserviceProject project, Map<String, String> properties, Map<String, String> outputEnvs) {
        LOGGER.fine("Looking for build files in project: " + project.getName());

        // Look for a pom.xml file
        if (lookForFile(project, properties, "pom.xml")) {
            LOGGER.fine("Found pom.xml file");

            // determine strategies that will yield a successful maven build and deployment.
            // In particular:
            //   * Is there any build plugin to generate a dockerfile?
            //   * Is there configuration to generate an executable jar file?
            introspectPomXml(project, properties.get("pom.xml"), outputEnvs);
        }

        // Look for a build.gradle file
        if (lookForFile(project, properties, "build.gradle")) {
            LOGGER.fine("Found build.gradle file");
            // determine strategies that will yield a successful gradle build and deployment
            introspectBuildGradle(project, properties.get("build.gradle"), outputEnvs);
        }

        // Dockerfile and compose.yaml files
//        boolean hasDocker = false;
        if (lookForFile(project, properties, "Dockerfile")) {
            // with this strategy, we can short circuit and just substitute our spring project to instead be a Docker project.

            strategies.add(
                new DeploymentStrategy(DeploymentStrategy.DeploymentType.DOCKER_FILE, 1000)
                    .withCommand(new String[] { properties.get("Dockerfile") }));
        }
//        hasDocker |= lookForFile(project, properties, "docker-compose.yaml");
//        hasDocker |= lookForFile(project, properties, "compose.yaml");
//        if (hasDocker) {
//            LOGGER.fine("Found docker files");
//            // determine strategies that will yield a successful docker build and deployment
//        }
    }

    private void introspectPomXml(MicroserviceProject project, String pomXmlPath, Map<String, String> outputEnvs) {
        // look to see if the pom uses the spring-boot-maven-plugin plugin to generate far jars
        // Look for dockerfile support in the pom

        try {
            DeploymentStrategy deploymentStrategy = new DeploymentStrategy(DeploymentStrategy.DeploymentType.MAVEN_POM, 2100);
            File inputFile = new File(pomXmlPath);
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader(inputFile));

            if (findPlugin(model, "com.spotify:dockerfile-maven-plugin").isPresent()) {
                LOGGER.fine("Found Spotify Dockerfile Maven Plugin!");
                LOGGER.fine("Use the following command to build the Docker image:");
                LOGGER.fine("mvn package dockerfile:build");
                deploymentStrategy.withCommand(new String[] { "mvn", "package", "dockerfile:build" });
            } else if (findPlugin(model, "io.fabric8:docker-maven-plugin").isPresent()) {
                LOGGER.fine("Found Fabric8 Docker Maven Plugin!");
                LOGGER.fine("Use the following command to build the Docker image:");
                LOGGER.fine("mvn package docker:build");
                deploymentStrategy.withCommand(new String[] { "mvn", "package", "docker:build" });
            }

            Optional<Plugin> springBootMavenPluginOptional = findPlugin(model, "org.springframework.boot:spring-boot-maven-plugin");
            if (springBootMavenPluginOptional.isPresent()) {
                LOGGER.fine("Found Spring Boot Maven Plugin!");
                Plugin plugin = springBootMavenPluginOptional.get();

                // detect whether it's a web application
                if (findDependency(model, "org.springframework.boot:spring-boot-starter-web").isPresent()) {
                    LOGGER.fine("Found Spring Boot Starter Web dependency!");
                    outputEnvs.put("SERVER_PORT", DEFAULT_SERVER_PORT);
                } else if (findDependency(model, "org.springframework.boot:spring-boot-starter-webflux").isPresent()) {
                    LOGGER.fine("Found Spring Boot Starter WebFlux dependency!");
                    outputEnvs.put("SERVER_PORT", DEFAULT_SERVER_PORT);
                }

                // determine the docker image name
                var dockerImageName = getPluginConfiguration(plugin, "image.name");
                if (dockerImageName == null) {
                    dockerImageName = getPluginConfiguration(plugin, "imageName");
                }
                if (dockerImageName == null) {
                    dockerImageName = model.getArtifactId() + ":" + model.getVersion();
                }
                if (dockerImageName != null && !dockerImageName.contains(":")) {
                    dockerImageName += ":latest";
                }
                outputEnvs.put("BUILD_IMAGE", dockerImageName);

                // OpenTelemetry Java Agent FIXME could be other APMs
                if (project.isOpenTelemetryEnabled()) {
                    String buildpacks = getPluginConfiguration(plugin, "image.buildpacks");
                    // FIXME this doesn't cover all cases, but it's a start
                    if (buildpacks == null) {
                        LOGGER.fine("OpenTelemetry is enabled for [%s], but the Spring Boot Maven Plugin is not configured with buildpacks.".formatted(project.getName()));
                        outputEnvs.put("BUILD_ADD_OTEL_AGENT", "true");
                    }
                }
                // FIXME We could add more options here, like -Dspring-boot.build-image.imageName=...
                // https://docs.spring.io/spring-boot/maven-plugin/build-image.html#build-image.customization
                deploymentStrategy.withCommand(new String[] {"mvn", "spring-boot:build-image"});
            }

            strategies.add(deploymentStrategy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getPluginConfiguration(Plugin plugin, String propertyName) {
        Xpp3Dom configuration = (Xpp3Dom) plugin.getConfiguration();
        String value = getConfiguration(configuration, propertyName.split("\\."), 0);
        if (value != null) {
            LOGGER.fine("Found " + propertyName + " with value [" + value + "] in plugin configuration");
            return value;
        }

        for (PluginExecution execution : plugin.getExecutions()) {
            Xpp3Dom execConfiguration = (Xpp3Dom) execution.getConfiguration();
            value = getConfiguration(execConfiguration, propertyName.split("\\."), 0);
            if (value != null) {
                LOGGER.fine("Found " + propertyName + " with value [" + value + "] in plugin execution [" + execution.getId() + "] configuration");
                return value;
            }
        }
        return null;
    }

    private static String getConfiguration(Xpp3Dom dom, String[] properties, int i) {
        if (dom == null) {
            return null;
        }
        if (i >= dom.getChildCount()) {
            return null;
        }
        if (i == properties.length - 1) {
            Xpp3Dom child = dom.getChild(properties[i]);
            if (child == null) {
                return null;
            }
            return child.getValue();
        }

        return getConfiguration(dom.getChild(properties[i]), properties, i + 1);
    }

    private void introspectBuildGradle(MicroserviceProject project, String buildGradlePath, Map<String, String> outputEnvs) {
        // look to see if the pom uses the spring-boot-maven-plugin plugin to generate far jars
        // Look for dockerfile support in the pom

        boolean dockerPluginFound = false;
        DeploymentStrategy deploymentStrategy = new DeploymentStrategy(DeploymentStrategy.DeploymentType.GRADLE_BUILD, 2200);

        String springWebDependency = "org.springframework.boot:spring-boot-starter-web";
        String springWebFluxDependency = "org.springframework.boot:spring-boot-starter-webflux";
        try {
            boolean webDependencyFound = false;
            if (checkDependencyInGradleFile(buildGradlePath, springWebDependency)) {
                LOGGER.fine("Found Spring Boot Starter Web dependency!");
                webDependencyFound = true;
            } else if (checkDependencyInGradleFile(buildGradlePath, springWebFluxDependency)) {
                LOGGER.fine("Found Spring Boot Starter WebFlux dependency!");
                webDependencyFound = true;
            }
// FIXME this won't work, cause azd will simply execute whichever command we pass
//            List<String> command = new ArrayList<>();
//            command.add("gradle");
//            command.add("containerise");
//            if (webDependencyFound) {
//                command.add("--port " + DEFAULT_SERVER_PORT);
//                command.add(" --env SERVER_PORT=" + DEFAULT_SERVER_PORT);
//            }
//            deploymentStrategy.withCommand(command.toArray(new String[0]));
//            strategies.add(deploymentStrategy);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean checkDependencyInGradleFile(String filePath, String dependency) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        Pattern pattern = Pattern.compile(".*['\"]" + Pattern.quote(dependency) + "['\"].*");

        while ((line = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                reader.close();
                return true;
            }
        }

        reader.close();
        return false;
    }

    private boolean lookForFile(MicroserviceProject project, Map<String, String> properties, String fileName) {
        if (project.getPath() == null) {
            return false;
        }

        Path projectPath = Paths.get(project.getPath());
        Path filePath = projectPath.resolve(fileName);
        if (Files.exists(filePath)) {
            properties.put(fileName, filePath.toString());
            return true;
        }
        return false;
    }

    private void introspectJavaFiles(MicroserviceProject project) {
        if (project.getPath() == null) {
            return;
        }

        // Create a JavaParser instance
        JavaParser javaParser = new JavaParser();

        // Get the project path
        Path projectPath = Paths.get(project.getPath());

        // Walk the project directory and parse each .java file
        try (Stream<Path> paths = Files.walk(projectPath)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> parseAndVisit(javaParser, path.toFile()));
        } catch (IOException e) {
            LOGGER.severe("Failed to walk Spring project path '" + projectPath + "' relative to working directory.");
//            e.printStackTrace();
        }
    }

    private void parseAndVisit(JavaParser javaParser, File file) {
        try {
            // Parse the .java file
            CompilationUnit cu = javaParser.parse(file).getResult().orElse(null);

            if (cu != null) {
                // Visit the parsed file
                cu.accept(new ValueAnnotationVisitor(), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ValueAnnotationVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(SingleMemberAnnotationExpr n, Void arg) {
            super.visit(n, arg);
            checkForValueAnnotation(n);
        }

        @Override
        public void visit(NormalAnnotationExpr n, Void arg) {
            super.visit(n, arg);
            checkForValueAnnotation(n);
        }

        @Override
        public void visit(MarkerAnnotationExpr n, Void arg) {
            super.visit(n, arg);
            checkForValueAnnotation(n);
        }

        private void checkForValueAnnotation(AnnotationExpr n) {
            // Check for @Value annotations
            if (n.getNameAsString().equals("Value")) {
                // TODO: Do something with the @Value annotation
                LOGGER.info("@Value annotation found: " + n);
            }
        }
    }

    /**
     * Find a plugin in the model by its coordinate.
     * @param model the Maven model
     * @param coordinate The coordinate of the plugin to find, e.g. "com.spotify:dockerfile-maven-plugin:1.0.0"
     * @return The plugin if found, otherwise an empty Optional
     */
    private static Optional<Plugin> findPlugin(Model model, String coordinate) {
        if (model == null || coordinate == null) {
            return Optional.empty();
        }
        String[] parts = coordinate.split(":");
        return model.getBuild().getPlugins().stream()
                .filter(plugin -> mavenPluginMatch(plugin, parts[0], parts[1]))
                .findFirst();
    }

    /**
     * Find a dependency in the model by its coordinate.
     * @param model the Maven model
     * @param coordinate The coordinate of the dependency to find, e.g. "org.springframework.boot:spring-boot-starter-web:5.0.0"
     * @return The dependency if found, otherwise an empty Optional
     */
    private static Optional<Dependency> findDependency(Model model, String coordinate) {
        if (model == null || coordinate == null) {
            return Optional.empty();
        }
        String[] parts = coordinate.split(":");
        return model.getDependencies().stream()
                .filter(dependency -> mavenDependencyMatch(dependency, parts[0], parts[1]))
                .findFirst();
    }

    private static boolean mavenPluginMatch(Plugin plugin, String groupId, String artifactId) {
        return groupId.equals(plugin.getGroupId()) && artifactId.equals(plugin.getArtifactId());
    }


    private static boolean mavenDependencyMatch(Dependency dependency, String groupId, String artifactId) {
        return groupId.equals(dependency.getGroupId()) && artifactId.equals(dependency.getArtifactId());
    }

}

package com.microsoft.aspire.extensions.spring.implementation;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.microsoft.aspire.extensions.spring.resources.SpringProject;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class SpringIntrospector {
    private static final Logger LOGGER = Logger.getLogger(SpringIntrospector.class.getName());

    private final Set<SpringDeploymentStrategy> strategies = new TreeSet<>();
    private String DEFAULT_SERVER_PORT = "8081";

    public SpringIntrospector() { }

    public Set<SpringDeploymentStrategy> introspect(SpringProject project, Map<String, String> outputEnvs) {
        LOGGER.info("Beginning introspection of Spring project: " + project.getName());

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

    private void lookForBuildFiles(SpringProject project, Map<String, String> properties, Map<String, String> outputEnvs) {
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
                new SpringDeploymentStrategy(SpringDeploymentStrategy.DeploymentType.DOCKER_FILE, 1000)
                    .withCommand(properties.get("Dockerfile")));
        }
//        hasDocker |= lookForFile(project, properties, "docker-compose.yaml");
//        hasDocker |= lookForFile(project, properties, "compose.yaml");
//        if (hasDocker) {
//            LOGGER.fine("Found docker files");
//            // determine strategies that will yield a successful docker build and deployment
//        }
    }

    private void introspectPomXml(SpringProject project, String pomXmlPath, Map<String, String> outputEnvs) {
        // look to see if the pom uses the spring-boot-maven-plugin plugin to generate far jars
        // Look for dockerfile support in the pom

        try {
            SpringDeploymentStrategy deploymentStrategy = new SpringDeploymentStrategy(SpringDeploymentStrategy.DeploymentType.MAVEN_POM, 2100);
            boolean dockerPluginFound = false;
            boolean springBootMavenPluginFound = false;
            String dockerImageName = null;
            File inputFile = new File(pomXmlPath);
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader(inputFile));

            String artifactId = model.getArtifactId();
            String version = model.getVersion();

            List<Plugin> plugins = model.getBuild().getPlugins();
            for (Plugin plugin : plugins) {
                if (mavenPluginMatch(plugin, "com.spotify", "dockerfile-maven-plugin")) {
                    LOGGER.fine("Found Spotify Dockerfile Maven Plugin!");
                    LOGGER.fine("Use the following command to build the Docker image:");
                    LOGGER.fine("mvn package dockerfile:build");
                    dockerPluginFound = true;
                    deploymentStrategy.withCommand("mvn package dockerfile:build");
                } else if (mavenPluginMatch(plugin, "io.fabric8", "docker-maven-plugin")) {
                    LOGGER.fine("Found Fabric8 Docker Maven Plugin!");
                    LOGGER.fine("Use the following command to build the Docker image:");
                    LOGGER.fine("mvn package docker:build");
                    dockerPluginFound = true;
                    deploymentStrategy.withCommand("mvn package docker:build");
                }

                if (mavenPluginMatch(plugin, "org.springframework.boot", "spring-boot-maven-plugin")) {
                    springBootMavenPluginFound = true;
                    dockerImageName = getPluginConfiguration(plugin, "image.name");
                    if (dockerImageName == null) {
                        dockerImageName = getPluginConfiguration(plugin, "imageName");
                    }
                    if (dockerImageName != null) {
                        if (!dockerImageName.contains(":")) {
                            dockerImageName += ":latest";
                        }
                        LOGGER.fine("Found Spring Boot Maven Plugin with image name: " + dockerImageName);
                    } else {
                        dockerImageName = artifactId + ":" + version;
                    }
                }
            }

            if (!dockerPluginFound) {
                LOGGER.fine("No Docker plugin found in the pom.xml file.");

                boolean webDependencyFound = false;
                List<Dependency> dependencies = model.getDependencies();
                for (Dependency dependency : dependencies) {
                    if (mavenDependencyMatch(dependency, "org.springframework.boot", "spring-boot-starter-web")) {
                        LOGGER.fine("Found Spring Boot Starter Web dependency!");
                        webDependencyFound = true;
                    } else if (mavenDependencyMatch(dependency, "org.springframework.boot", "spring-boot-starter-webflux")) {
                        LOGGER.fine("Found Spring Boot Starter WebFlux dependency!");
                        webDependencyFound = true;
                    }
                }

                if (webDependencyFound) {
                    outputEnvs.put("SERVER_PORT", DEFAULT_SERVER_PORT);
                }

                if (springBootMavenPluginFound) {
                    LOGGER.fine("Spring Boot Maven Plugin found in the pom.xml file.");
                    // FIXME We could add more options here, like -Dspring-boot.build-image.imageName=...
                    // https://docs.spring.io/spring-boot/maven-plugin/build-image.html#build-image.customization
                    deploymentStrategy.withCommand("mvn spring-boot:build-image");
                    outputEnvs.put("BUILD_IMAGE", dockerImageName);
                } else {
                    String command = "mvn containerise";
                    // command += " --context " + Paths.get(pomXmlPath).getParent();
                    if (webDependencyFound) {
                        command += " --port " + DEFAULT_SERVER_PORT;
                        command += " --env SERVER_PORT=" + DEFAULT_SERVER_PORT;
                    }

                    deploymentStrategy.withCommand(command);
                    outputEnvs.put("BUILD_IMAGE", artifactId + ":" + version);
                }

                strategies.add(deploymentStrategy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getPluginConfiguration(Plugin plugin, String propertyName) {
        Xpp3Dom configuration = (Xpp3Dom) plugin.getConfiguration();
        String value = getConfiguration(configuration, propertyName.split("\\."), 0);
        if (value != null) {
            return value;
        }

        for (PluginExecution execution : plugin.getExecutions()) {
            Xpp3Dom execConfiguration = (Xpp3Dom) execution.getConfiguration();
            value = getConfiguration(execConfiguration, propertyName.split("\\."), 0);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private static String getConfiguration(Xpp3Dom dom, String[] properties, int i) {
        if (dom == null) {
            return null;
        }
        if (i == properties.length - 1) {
            return dom.getChild(properties[i]).getValue();
        }

        return getConfiguration(dom.getChild(properties[i]), properties, i + 1);
    }

    private void introspectBuildGradle(SpringProject project, String buildGradlePath, Map<String, String> outputEnvs) {
        // look to see if the pom uses the spring-boot-maven-plugin plugin to generate far jars
        // Look for dockerfile support in the pom

        boolean dockerPluginFound = false;
        SpringDeploymentStrategy deploymentStrategy = new SpringDeploymentStrategy(SpringDeploymentStrategy.DeploymentType.GRADLE_BUILD, 2200);

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

            String command = "gradle containerise";
            if (webDependencyFound) {
                command += " --port " + DEFAULT_SERVER_PORT;
                command += " --env SERVER_PORT=" + DEFAULT_SERVER_PORT;
            }
            deploymentStrategy.withCommand(command);
            strategies.add(deploymentStrategy);
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

    private boolean lookForFile(SpringProject project, Map<String, String> properties, String fileName) {
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

    private void introspectJavaFiles(SpringProject project) {
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



    private static boolean mavenPluginMatch(Plugin plugin, String groupId, String artifactId) {
        return groupId.equals(plugin.getGroupId()) && artifactId.equals(plugin.getArtifactId());
    }


    private static boolean mavenDependencyMatch(Dependency dependency, String groupId, String artifactId) {
        return groupId.equals(dependency.getGroupId()) && artifactId.equals(dependency.getArtifactId());
    }


}

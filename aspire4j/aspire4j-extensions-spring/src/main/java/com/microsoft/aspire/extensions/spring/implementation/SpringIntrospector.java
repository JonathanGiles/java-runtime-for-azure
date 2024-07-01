package com.microsoft.aspire.extensions.spring.implementation;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.microsoft.aspire.extensions.spring.SpringProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class SpringIntrospector {
    private static final Logger LOGGER = Logger.getLogger(SpringIntrospector.class.getName());

    private final Set<SpringDeploymentStrategy> strategies = new TreeSet<>();

    public SpringIntrospector() { }

    public Set<SpringDeploymentStrategy> introspect(SpringProject project) {
        LOGGER.info("Beginning introspection of Spring project: " + project.getName());

        /*
         * This is where we can look at the project and do things like:
         * - Look for a Dockerfile
         * - Look for a pom.xml or build.gradle file for any known configuration or dependencies
         * - Look for configuration files in known locations
         * - Parse Java source code to look for Spring annotations
         */
        Map<String, String> introspectionProperties = new LinkedHashMap<>();

        lookForBuildFiles(project, introspectionProperties);
        introspectJavaFiles(project);

        // TODO Take the introspection properties and turn it into a set of useful properties to pass to the aspire-manifest,
        // and modify the properties of the SpringProject directly
//        Map<String, String> properties = new LinkedHashMap<>();

        return strategies;
    }

    private void lookForBuildFiles(SpringProject project, Map<String, String> properties) {
        LOGGER.fine("Looking for build files in project: " + project.getName());

        // Look for a pom.xml file
        if (lookForFile(project, properties, "pom.xml")) {
            LOGGER.fine("Found pom.xml file");

            // determine strategies that will yield a successful maven build and deployment.
            // In particular:
            //   * Is there any build plugin to generate a dockerfile?
            //   * Is there configuration to generate an executable jar file?
            introspectPomXml(project, properties.get("pom.xml"));
        }

        // Look for a build.gradle file
        if (lookForFile(project, properties, "build.gradle")) {
            LOGGER.fine("Found build.gradle file");
            // determine strategies that will yield a successful gradle build and deployment
        }

        // Dockerfile and compose.yaml files
//        boolean hasDocker = false;
        if (lookForFile(project, properties, "Dockerfile")) {
            // with this strategy, we can short circuit and just substitute our spring project to instead be a Docker project.
            strategies.add(new SpringDeploymentStrategy(SpringDeploymentStrategy.DeploymentType.DOCKER_FILE, 1000).withCommand("Dockerfile"));
        }
//        hasDocker |= lookForFile(project, properties, "docker-compose.yaml");
//        hasDocker |= lookForFile(project, properties, "compose.yaml");
//        if (hasDocker) {
//            LOGGER.fine("Found docker files");
//            // determine strategies that will yield a successful docker build and deployment
//        }
    }

    private void introspectPomXml(SpringProject project, String s) {
        // look to see if the pom uses the spring-boot-maven-plugin plugin to generate far jars


        // Look for dockerfile support in the pom
        try {
            File inputFile = new File("pom.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("plugin");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String groupId = eElement.getElementsByTagName("groupId").item(0).getTextContent();
                    String artifactId = eElement.getElementsByTagName("artifactId").item(0).getTextContent();

                    if ("com.spotify".equals(groupId) && "dockerfile-maven-plugin".equals(artifactId)) {
                        System.out.println("Found Spotify Dockerfile Maven Plugin!");
                        System.out.println("Use the following command to build the Docker image:");
                        System.out.println("mvn package dockerfile:build");
                    } else if ("io.fabric8".equals(groupId) && "docker-maven-plugin".equals(artifactId)) {
                        System.out.println("Found Fabric8 Docker Maven Plugin!");
                        System.out.println("Use the following command to build the Docker image:");
                        System.out.println("mvn package docker:build");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            e.printStackTrace();
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
}

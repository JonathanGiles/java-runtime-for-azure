FROM maven:3.9.7-eclipse-temurin-21-alpine AS maven
MAINTAINER microsoft.com
COPY src storage-explorer/src
COPY pom.xml storage-explorer/
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar
RUN mvn package -f storage-explorer/pom.xml
RUN cp storage-explorer/target/*.jar /storage-explorer.jar
ENTRYPOINT ["java","-javaagent:/opentelemetry-javaagent.jar", "-jar", "/storage-explorer.jar"]
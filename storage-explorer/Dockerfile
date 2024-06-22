FROM maven:3.9.7-eclipse-temurin-21-alpine AS maven
MAINTAINER microsoft.com
COPY storage-explorer/src storage-explorer/src
COPY storage-explorer/pom.xml storage-explorer/
RUN mvn package -f storage-explorer/pom.xml
RUN cp storage-explorer/target/*.jar /storage-explorer.jar
ENTRYPOINT ["java","-jar","/storage-explorer.jar"]
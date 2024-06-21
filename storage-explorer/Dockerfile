FROM maven:3.9.7-eclipse-temurin-21-alpine AS maven
MAINTAINER microsoft.com
COPY src app/src
COPY pom.xml app/
RUN mvn package -f app/pom.xml
RUN cp app/target/*.jar /storage-explorer.jar
ENTRYPOINT ["java","-jar","/storage-explorer.jar"]
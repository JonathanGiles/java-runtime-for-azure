FROM maven:3.9.7-eclipse-temurin-21 AS maven
MAINTAINER microsoft.com
COPY src eureka-service/src
COPY pom.xml eureka-service/
RUN mvn package -f eureka-service/pom.xml
RUN cp eureka-service/target/*.jar /eureka-service.jar
ENTRYPOINT ["java","-jar","/eureka-service.jar"]
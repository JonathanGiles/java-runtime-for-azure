FROM maven:3.9.7-eclipse-temurin-21-alpine AS maven
MAINTAINER microsoft.com
COPY date-service/src date-service/src
COPY date-service/pom.xml date-service/
RUN mvn package -f date-service/pom.xml
RUN cp date-service/target/*.jar /date-service.jar
ENTRYPOINT ["java","-jar","/date-service.jar"]
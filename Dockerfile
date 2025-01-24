# Use a base image with OpenJDK
FROM openjdk:17-jdk-slim

# Add the Spring Boot jar file
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]

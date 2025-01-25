# Use a base image with OpenJDK
FROM openjdk:17-jdk-slim
EXPOSE 8080

# Add the Spring Boot jar file
ADD target/audio-storage-app.jar audio-storage-app.jar
# Run the application
ENTRYPOINT ["java", "-jar", "/audio-storage-app.jar"]

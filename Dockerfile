# Use a base image with JDK installed
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the build files
COPY . .
RUN chmod +x gradlew
# Build the application, skipping tests
RUN ./gradlew clean build -x test

# Copy the JAR file to the container
COPY build/libs/hms-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
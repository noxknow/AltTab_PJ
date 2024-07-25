# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Package the application
RUN ./gradlew build

# Make port 8081 available to the world outside this container
EXPOSE 8080

# Run the jar file
CMD ["java", "-jar", "build/libs/compiler-0.0.1-SNAPSHOT.jar"]
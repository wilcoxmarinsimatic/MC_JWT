FROM eclipse-temurin:17-jdk-focal AS develop
# Set the working directory
WORKDIR /app
# Copy the Maven POM file
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Install dependenciasa
RUN ./mvnw dependency:go-offline
# COPY src ./src
COPY src ./src
# Command to run the application
CMD ["./mvnw", "spring-boot:run"]

# # Use a base image with Java installed
FROM maven:3.8.4-openjdk-17-slim AS build
# Set the working directory
WORKDIR /app
# Copy the Maven POM file
COPY pom.xml ./
# Copy the source code
COPY src ./src
# Build the application
RUN mvn clean package -DskipTests
RUN ls /app/target


# Use a smaller base image for the final application
FROM openjdk:17.0.2-jdk-slim as production
# Set the working directory for the final image
WORKDIR /app
# Copy the built JAR file from the previous stage
COPY --from=build /app/target/*.jar mc_jwt-0.0.1-SNAPSHOT.jar
RUN ls /app
# Expose the port your app runs on
EXPOSE 8080
# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/mc_jwt-0.0.1-SNAPSHOT.jar"]
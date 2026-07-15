# Stage 1: Build the Maven application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and download dependencies to cache them
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the built WAR file from the build stage
COPY --from=build /app/target/CODELUX_Backend-0.0.1-SNAPSHOT.war app.war

# Expose port 8080 (matching default Spring Boot port)
EXPOSE 8080

# Run the executable WAR file
ENTRYPOINT ["java", "-jar", "app.war"]

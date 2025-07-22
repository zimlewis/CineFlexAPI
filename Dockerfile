FROM eclipse-temurin:24-jdk AS builder

# Set the working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for dependency caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the source code
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# === Runtime stage ===
FROM eclipse-temurin:24-jdk

# Set the working directory
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
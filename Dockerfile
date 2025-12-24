# Stage 1: Build with Maven
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Lightweight Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
# server.port=${PORT} allows Render to assign the port dynamically
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT}"]
# Stage 1: Build
FROM maven:3.9.11-amazoncorretto-21-alpine AS build
WORKDIR /app

# Copy and build events-library first
COPY events-library ./events-library
WORKDIR /app/events-library
RUN mvn clean install -B

# Copy authenticationservice and build
WORKDIR /app/authenticationservice
COPY authenticationservice/pom.xml .

COPY authenticationservice/src ./src
RUN mvn clean install -B -DskipTests

# Stage 2: Runtime
FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY --from=build /app/authenticationservice/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

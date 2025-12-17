# Stage 1: Build
FROM maven:3.9.11-amazoncorretto-21-alpine AS build
WORKDIR /app

# 1️⃣ Copy only pom.xml first to cache dependencies
COPY pom.xml .

# 2️⃣ Download dependencies
RUN mvn dependency:go-offline -B

# 3️⃣ Copy source code
COPY src ./src

# 4️⃣ Build the jar (skip tests for faster builds)
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM amazoncorretto:21-alpine-jdk
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
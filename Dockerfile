# ===== BUILD =====
FROM maven:3.9.9-eclipse-temurin-21 AS build
LABEL authors="linnlathtun"

WORKDIR /app

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests clean package

# ===== RUN =====
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Render provides PORT env var automatically
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --server.port=${PORT:-8080}"]

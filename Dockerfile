# === Build Stage ===
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

# The "builder" name here must match the "AS builder" name above
COPY --from=builder /app/target/*.jar server.jar

EXPOSE 8085
CMD ["java", "-jar", "server.jar"]
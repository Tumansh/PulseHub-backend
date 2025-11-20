FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build

# Copy entire backend so parent reactor works
COPY ../ ..

# Verify modules (optional but helpful)
RUN mvn -q -DskipTests help:modules

# Build only ai-service
RUN mvn clean package -DskipTests -pl :ai-service -am

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /build/ai-service/target/*.jar app.jar

EXPOSE 8087
ENTRYPOINT ["java","-jar","app.jar"]

# Stage 1 - Build
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build

# Copy parent pom and service pom
COPY backend/pom.xml ./pom.xml
COPY backend/ai-service/pom.xml ./ai-service/pom.xml

# Download dependencies
RUN mvn -B -q dependency:go-offline

# Copy source
COPY . .

# Build only ai-service
RUN mvn clean package -DskipTests -pl ai-service -am


# Stage 2 - Run
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /build/backend/ai-service/target/*.jar app.jar

EXPOSE 8087
ENTRYPOINT ["java","-jar","app.jar"]

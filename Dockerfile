FROM openjdk:17-jdk-slim

# Install xargs
RUN apt-get update && apt-get install -y findutils

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew

# Test gradlew to ensure it has correct permissions and dependencies
RUN ./gradlew --version

RUN ./gradlew build --exclude-task test

# Use a specific name for the jar file to avoid cp command error
RUN find ./build/libs -name "*.jar" -exec mv {} /app.jar \;

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]

FROM openjdk:17-jdk
COPY build/libs/donut-0.0.1-SNAPSHOT.jar ddanzi.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/ddanzi.jar"]

FROM openjdk:17-jdk
COPY build/libs/ddanzi-0.0.1-SNAPSHOT.jar ddanzi.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/ddanzi.jar"]

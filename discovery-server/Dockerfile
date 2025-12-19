FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/discovery-server.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]

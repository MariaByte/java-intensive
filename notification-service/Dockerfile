FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/notification-service.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]

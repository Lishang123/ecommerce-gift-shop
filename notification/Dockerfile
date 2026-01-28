FROM eclipse-temurin:21-jdk-ubi9-minimal
LABEL authors="houcai"
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
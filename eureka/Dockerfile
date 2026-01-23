FROM eclipse-temurin:21-jdk-ubi9-minimal
LABEL authors="houcai"
WORKDIR app.jar
COPY target/*.jar app.jar
EXPOSE 7777
ENTRYPOINT ["java", "-jar", "app.jar"]
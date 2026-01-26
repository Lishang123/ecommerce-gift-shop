#!/bin/bash

cd ..

# Build all services
cd order && ./mvnw clean package -DskipTests && cd ..
cd user && ./mvnw clean package -DskipTests && cd ..
cd product && ./mvnw clean package -DskipTests && cd ..
cd notification && ./mvnw clean package -DskipTests && cd ..
cd eureka && ./mvnw clean package -DskipTests && cd ..
cd gateway && ./mvnw clean package -DskipTests && cd ..
cd config-server && ./mvnw clean package -DskipTests && cd ..
FROM maven:3.9.15-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN groupadd --system grupo-app && useradd --system --gid grupo-app usuario-app
USER usuario-app:grupo-app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
LABEL authors="ferreira"

ENTRYPOINT ["java", "-jar", "app.jar"]
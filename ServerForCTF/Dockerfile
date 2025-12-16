# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Копируем jar с точным именем (если у тебя другой, поменяй)
COPY --from=build /app/target/ServerForCTF-0.0.1-SNAPSHOT.jar app.jar

# Настройки памяти для Railway (уменьшено для OOM)
ENV JAVA_OPTS="-Xms64m -Xmx256m -XX:+UseG1GC -XX:MaxGCPauseMillis=100"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

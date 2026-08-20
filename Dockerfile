FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./gradlew build --no-daemon

FROM eclipse-temurin:25-jdk-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/agendador-tarefas.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/agendador-tarefas.jar"]
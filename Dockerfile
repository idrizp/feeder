FROM gradle:latest as builder
ARG MODULE
WORKDIR /app
COPY . /app
RUN gradle ${MODULE}:shadowJar
RUN ls -la /app

FROM openjdk:21-slim
ARG MODULE
WORKDIR /app
COPY --from=builder /app/${MODULE}/build/libs/*-all.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
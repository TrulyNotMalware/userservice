#Dockerfile to builds loggers.
FROM eclipse-temurin:17.0.5_8-jdk as builder
RUN apt update && apt install curl -y
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJar
#RUN ./gradlew jibDockerBuild

FROM eclipse-temurin:17.0.5_8-jdk
ARG JAR_FILE_NAME=userservice-0.0.1-SNAPSHOT
ARG CONFIG_FILE_NAME=application.yaml
ENV CONFIG_PATH = ${CONFIG_FILE_NAME}
ARG SERVER_PORT=80
ARG ACTIVE_PROFILE=prod
ENV PROFILE = ${ACTIVE_PROFILE}

COPY --from=builder /build/libs/$JAR_FILE_NAME.jar /app.jar
#COPY build/libs/*.jar app.jar
ENV JAVA_OPTS=""
EXPOSE $SERVER_PORT
ENTRYPOINT exec java -jar -Dspring.profiles.active=${PROFILE} -Dspring.config.location=classpath:/${CONFIG_PATH} /app.jar

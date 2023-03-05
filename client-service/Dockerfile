FROM openjdk:11-jdk-slim
LABEL Description="Client application" Version="1.0"

ENV APP_JAR_NAME="client-service-1.0-SNAPSHOT.jar"
COPY target/$APP_JAR_NAME $APP_JAR_NAME

ENTRYPOINT exec java -jar $APP_JAR_NAME

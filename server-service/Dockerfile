FROM openjdk:11-jdk-slim
LABEL Description="Server application" Version="1.0"

ENV APP_JAR_NAME="server-service-1.0-SNAPSHOT.jar"
COPY target/$APP_JAR_NAME $APP_JAR_NAME

ENTRYPOINT exec java -jar $APP_JAR_NAME

FROM amazoncorretto:17-alpine3.16

WORKDIR /app

ARG JAR_FILE=target/crm-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
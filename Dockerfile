FROM openjdk:17
COPY target/to-do-list-coursework-0.0.1.jar app.jar
ENTRYPOINT java -jar app.jar

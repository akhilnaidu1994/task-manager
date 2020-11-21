FROM openjdk:8

WORKDIR /usr/app

COPY ./target/task-manager.jar .

EXPOSE 8080

RUN ["java", "-jar", "task-manager.jar"]
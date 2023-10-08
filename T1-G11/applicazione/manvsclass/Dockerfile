#FROM openjdk:8-alpine as java8
FROM ubuntu:latest

RUN apt-get update && apt-get install -y openjdk-8-jdk bash

#RUN apt-get update && apt-get install -y bash
RUN apt-get update && apt-get install -y openssl libncurses5 libstdc++6

COPY target/manvsclass-0.0.1-SNAPSHOT.jar /app/manvsclass.jar

WORKDIR /app

EXPOSE 8080
CMD ["java","-jar","manvsclass.jar"]
ENTRYPOINT [ "bash", "./docker-entrypoint.sh" ]
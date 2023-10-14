FROM ubuntu:latest

RUN apt-get update && apt-get install -y openjdk-17-jdk bash
# Install Java 8
RUN apt-get update && apt-get install -y openjdk-8-jdk


ARG JAR_FILE=target/RemoteCCC-0.0.1.jar
COPY ${JAR_FILE} app.jar
COPY ClientProject ClientProject 
COPY entrypoint.sh /entrypoint.sh
ENTRYPOINT ["java", "-jar", "/app.jar"]
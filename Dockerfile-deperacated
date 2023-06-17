FROM openjdk:17-jdk-slim
COPY  ./target/elasticsearch-0.0.1-SNAPSHOT.jar /usr/src/app/
WORKDIR /usr/src/app
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "elasticsearch-0.0.1-SNAPSHOT.jar"]
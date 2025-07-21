FROM openjdk:latest
COPY ./target/seMethods-1.0-jar-with-dependencies.jar /tmp/sem.jar
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "sem.jar"]

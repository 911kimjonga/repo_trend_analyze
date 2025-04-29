# Base image
FROM openjdk:17-jdk-alpine

COPY ../build.gradle /build/

COPY ../src /build/src/

WORKDIR /build/

FROM openjdk:17-jdk

ADD ../build/libs/fortune-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
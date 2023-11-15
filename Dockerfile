FROM openjdk:11.0.11-jdk
MAINTAINER oxerek

ARG jarFileName

RUN adduser --system --group spring
USER spring
COPY $jarFileName /home/spring/app.jar
WORKDIR /home/spring

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
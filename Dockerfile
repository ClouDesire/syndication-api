FROM eclipse-temurin:17-jre-alpine
MAINTAINER ClouDesire <dev@cloudesire.com>

ADD target/syndication-api.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

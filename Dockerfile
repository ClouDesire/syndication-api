FROM eclipse-temurin:21-alpine@sha256:4909fb9ab52e3ce1488cc6e6063da71a0f9f9833420cc254fe03bbe25daec9e0
LABEL org.opencontainers.image.authors="Cloudesire dev team <cloudesire-dev@eng.it>"

ADD target/syndication-api.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

FROM eclipse-temurin:21-alpine@sha256:5b836a84d8287dcba9c89beb5a449871430206b8ff758a7732f2e43313c0fbd4
LABEL org.opencontainers.image.authors="Cloudesire dev team <cloudesire-dev@eng.it>"

ADD target/syndication-api.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

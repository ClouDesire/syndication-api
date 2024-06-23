FROM eclipse-temurin:21-alpine@sha256:cd77ea279fa771540f5ddac4fba3787cfdfd527adeb80b6b8a3aadbf78cfa4ea
LABEL org.opencontainers.image.authors="Cloudesire dev team <cloudesire-dev@eng.it>"

ADD target/syndication-api.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

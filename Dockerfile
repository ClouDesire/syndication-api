FROM eclipse-temurin:21-alpine@sha256:68a8a4ad547e750f497824540d90ff29d4b819a6a6287a5eb1b03a71e4c2167b
LABEL org.opencontainers.image.authors="Cloudesire dev team <cloudesire-dev@eng.it>"

ADD target/syndication-api.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

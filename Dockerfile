FROM eclipse-temurin:21-alpine@sha256:cf94706ed7b63f1f29b720182fe3385f2fd5d17b3a20ff60163ea480572d34c7
LABEL org.opencontainers.image.authors="Cloudesire dev team <cloudesire-dev@eng.it>"

ADD target/syndication-api.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

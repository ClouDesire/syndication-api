FROM eclipse-temurin:21-alpine@sha256:a3ef08aadbf2d925a6af28ab644f9974df9bd053d3728caa4b28329ae968e7ad
LABEL org.opencontainers.image.authors="Cloudesire dev team <cloudesire-dev@eng.it>"

ADD target/syndication-api.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

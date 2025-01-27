FROM eclipse-temurin:21-alpine@sha256:ee09bfd4218b1296231588981fd1e4f74843ca585d5fd6a37bf6078e34c847c7
LABEL org.opencontainers.image.authors="Cloudesire dev team <cloudesire-dev@eng.it>"

ADD target/syndication-api.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

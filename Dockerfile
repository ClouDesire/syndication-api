FROM eclipse-temurin:21-alpine@sha256:511d5a9217ed753d9c099d3d753111d7f9e0e40550b860bceac042f4e55f715c
LABEL org.opencontainers.image.authors="Cloudesire dev team <cloudesire-dev@eng.it>"

ADD target/syndication-api.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

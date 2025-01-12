FROM eclipse-temurin:21-alpine@sha256:56ffb38891c7ea074c1dd42cc9a978206b7c2752589f8f613e383050a8af0e50
LABEL org.opencontainers.image.authors="Cloudesire dev team <cloudesire-dev@eng.it>"

ADD target/syndication-api.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

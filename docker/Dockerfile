FROM cloudesire/java:8
MAINTAINER ClouDesire <dev@cloudesire.com>

ADD syndication-api.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

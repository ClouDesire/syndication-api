# syndication-api

[![CircleCI](https://circleci.com/gh/ClouDesire/syndication-api.svg?style=svg)](https://circleci.com/gh/ClouDesire/syndication-api)

An example Spring Boot 3.x project to implement the [Cloudesire syndicated
integration](https://docs.cloudesire.com/docs/syndication.html) to enable
automatic provisioning of a SaaS application on a
[Cloudesire](https://cloudesire.com) marketplace.

## Build

Project uses Maven to compile artifact and build a docker image with:

```
./mvnw clean install
```

## Run


Execute the container with:

```
docker run cloudesire/syndication-api:1.0.0-SNAPSHOT
```


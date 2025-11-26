# syndication-api

[![CircleCI](https://circleci.com/gh/ClouDesire/syndication-api.svg?style=svg)](https://circleci.com/gh/ClouDesire/syndication-api)

An example Spring Boot 4.x project to implement the [Cloudesire syndicated
integration][syndication] to enable automatic provisioning of a SaaS
application on a [Cloudesire][cloudesire] marketplace.

[syndication]: https://docs.cloudesire.com/docs/syndication.html
[cloudesire]: https://cloudesire.com

## Build

Project uses Maven to compile artifact and build a docker image with:

```sh
./mvnw clean package
```

## Run

Execute the container with:

```sh
docker run cloudesire/syndication-api:1.0.0-SNAPSHOT
```

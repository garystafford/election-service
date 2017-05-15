[![Build Status](https://travis-ci.org/garystafford/election-service.svg?branch=rabbitmq)](https://travis-ci.org/garystafford/election-service) [![Layers](https://images.microbadger.com/badges/image/garystafford/election-service.svg)](https://microbadger.com/images/garystafford/election-service "Get your own image badge on microbadger.com") [![Version](https://images.microbadger.com/badges/version/garystafford/election-service.svg)](https://microbadger.com/images/garystafford/election-service "Get your own version badge on microbadger.com")

# Voter Service

## Introduction

The Election [Spring Boot](https://projects.spring.io/spring-boot/) Service is a RESTful Web Service, backed by [MongoDB](https://www.mongodb.com/). The Election service exposes several HTTP API endpoints, listed below. API users can manage elections, and inspect technical information about the running service. API users can also create sample elections by calling the `/election/simulation` endpoint.

## Quick Start for Local Development

The Election service requires MongoDB to be running locally, on port `27017`, RabbitMQ running on `5672` and `15672`, and the Candidate service to be running on `8095`. To clone, build, test, and run the Voter service as a JAR file, locally:

```bash
git clone --depth 1 --branch rabbitmq \
  https://github.com/garystafford/election-service.git
cd election-service
./gradlew clean cleanTest build
java -jar build/libs/election-service-0.3.0.jar
```
## Quick Start with Docker
The easiest way to run the Voter API services locally is using the `docker-compose-local.yml` file, included in the project. The Docker Compose file will spin up single container instances of the Election service, Voter service, Candidate service, RabbitMQ, MongoDB, and the Voter API Gateway.

```bash
sh ./stack-deploy-local.sh
```

```text
CONTAINER ID        IMAGE                                     COMMAND                  CREATED             STATUS              PORTS                                                                                        NAMES
32d73282ff3d        garystafford/voter-api-gateway:latest     "/docker-entrypoin..."   8 seconds ago       Up 5 seconds        0.0.0.0:8080->8080/tcp                                                                       voterstack_voter-api-gateway_1
1ece438c5da4        garystafford/candidate-service:rabbitmq   "java -Dspring.pro..."   10 seconds ago      Up 7 seconds        0.0.0.0:8097->8097/tcp                                                                       voterstack_candidate_1
30391faa3422        garystafford/voter-service:rabbitmq       "java -Dspring.pro..."   10 seconds ago      Up 7 seconds        0.0.0.0:8099->8099/tcp                                                                       voterstack_voter_1
35063ccfe706        garystafford/election-service:rabbitmq    "java -Dspring.pro..."   12 seconds ago      Up 10 seconds       0.0.0.0:8095->8095/tcp                                                                       voterstack_election_1
23eae86967a2        rabbitmq:management-alpine                "docker-entrypoint..."   14 seconds ago      Up 11 seconds       4369/tcp, 5671/tcp, 0.0.0.0:5672->5672/tcp, 15671/tcp, 25672/tcp, 0.0.0.0:15672->15672/tcp   voterstack_rabbitmq_1
7e77ddecddbb        mongo:latest                              "docker-entrypoint..."   24 seconds ago      Up 21 seconds       0.0.0.0:27017->27017/tcp                                                                     voterstack_mongodb_1
```

## Getting Started with the Voter API
The easiest way to get started with the Voter API, using [HTTPie](https://httpie.org/) from the command line:  
1. Create sample elections: `http http://localhost:8095/election/simulation`  
2. View sample elections: `http http://localhost:8095/election/elections`  
3. Create sample candidates: `http http://localhost:8097/candidate/simulation`  
4. View sample candidates: `http http://localhost:8097/candidate/candidates/summary/election/2016%20Presidential%20Election`  
5. Create sample voter data: `http http://localhost:8099/voter/simulation/election/2016%20Presidential%20Election`  
6. View sample voter results: `http http://localhost:8099/voter/results`

Alternately, for step 5 above, you can use service-to-service RPC IPC with RabbitMQ, to retrieve the candidates:  
`http http://localhost:8099/voter/simulation/rpc/election/2016%20Presidential%20Election`

Alternately, for step 5 above, you can use eventual consistency using RabbitMQ, to retrieve the candidates from MongoDB:  
`http http://localhost:8099/voter/simulation/db/election/2016%20Presidential%20Election`

## Election Service Endpoints

The service uses a context path of `/election`. All endpoints must be are prefixed with this sub-path.

Purpose                                                                                                                  | Method  | Endpoint
------------------------------------------------------------------------------------------------------------------------ | :------ | :-----------------------------------------------------
Create Sample Elections                                                                                                  | GET     | [/election/simulation](http://localhost:8099/election/simulation)
Create Election                                                                                                          | POST    | [/election/elections](http://localhost:8099/election/elections)
View Elections                                                                                                           | GET     | [/election/elections](http://localhost:8099/election/elections)
Service Info                                                                                                             | GET     | [/election/info](http://localhost:8099/election/info)
Service Health                                                                                                           | GET     | [/election/health](http://localhost:8099/election/health)
Service Metrics                                                                                                          | GET     | [/election/metrics](http://localhost:8099/election/metrics)
Other [Spring Actuator](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready) endpoints | GET     | `/actuator`, `/mappings`, `/env`, `/configprops`, etc.
Other [HATEOAS](https://spring.io/guides/gs/rest-hateoas) endpoints for `/election/elections`                            | Various | DELETE, PATCH, PUT, page sort, size, etc.

The [HAL Browser](https://github.com/mikekelly/hal-browser) API browser for the `hal+json` media type is installed alongside the service. It can be accessed at `http://localhost:8099/election/actuator/`.

## Elections

Creating a new election requires an HTTP `POST` request to the `/election/elections` endpoint, as follows:

Using [HTTPie](https://httpie.org/) command line HTTP client.

```text
http POST http://localhost:8095/election/elections \
  date='2008-11-04' \
  electionType='FEDERAL' \
  title='2008 Presidential Election'
```

## Sample Output

`http POST http://localhost:8095/election/elections \
  date='2008-11-04' \
  electionType='FEDERAL' \
  title='2008 Presidential Election'`

```text
HTTP/1.1 201
Access-Control-Allow-Credentials: true
Access-Control-Allow-Headers: Content-Type, Accept, X-Requested-With, remember-me
Access-Control-Allow-Methods: POST, GET, OPTIONS, DELETE
Access-Control-Max-Age: 3600
Content-Type: application/json;charset=UTF-8
Date: Mon, 15 May 2017 00:05:57 GMT
Location: http://localhost:8095/election/elections/5918f0e51162e16da6d1cb8c
Transfer-Encoding: chunked
X-Application-Context: Election Service:8095
```

```json
{
    "_links": {
        "election": {
            "href": "http://localhost:8095/election/elections/5918f0e51162e16da6d1cb8c"
        },
        "self": {
            "href": "http://localhost:8095/election/elections/5918f0e51162e16da6d1cb8c"
        }
    },
    "date": "2008-11-04T00:00:00.000+0000",
    "electionType": "FEDERAL",
    "title": "2008 Presidential Election"
}
```

## Continuous Integration

The project's source code is continuously built and tested on every commit to [GitHub](https://github.com/garystafford/election-service), using [Travis CI](https://travis-ci.org/garystafford/election-service). If all unit tests pass, the resulting Spring Boot JAR is pushed to the `build-artifacts` branch of the [election-service](https://github.com/garystafford/election-service/tree/build-artifacts) GitHub repository. The JAR's filename is incremented with each successful build (i.e. `election-service-0.3.10.jar`).

![Vote Continuous Integration Pipeline](Voter-CI.png)

## Spring Profiles

The Election service includes several Spring Boot Profiles, in a multi-profile YAML document: `src/main/resources/application.yml`. The profiles are `default`, `docker-development`, `docker-production`, and `aws-production`. You will need to ensure your MongoDB instance is available at that `host` address and port of the profile you choose, or you may override the profile's properties.


```yaml
endpoints:
  enabled: true
  sensitive: false
info:
  java:
    source: "${java.version}"
logging:
  level:
    root: INFO
management:
  info:
    build:
      enabled: true
    git:
      mode: full
server:
  port: 8095
  context-path: /election
spring:
  application:
    name: Election Service
  data:
    mongodb:
      database: elections
      host: localhost
      port: 27017
  rabbitmq:
    host: localhost
---
spring:
  data:
    mongodb:
      host: mongodb
  rabbitmq:
    host: rabbitmq
  profiles: docker-local
---
endpoints:
  candidate:
    host: election
  enabled: false
  health:
    enabled: true
  info:
    enabled: true
  sensitive: true
logging:
  level:
    root: WARN
management:
  info:
    build:
      enabled: false
    git:
      enabled: false
spring:
  data:
    mongodb:
      host: "10.0.1.6"
  rabbitmq:
    host: "10.0.1.8"
  profiles: aws-production
---
endpoints:
  enabled: false
  health:
    enabled: true
  info:
    enabled: true
  sensitive: true
logging:
  level:
    root: WARN
management:
  info:
    build:
      enabled: false
    git:
      enabled: false
spring:
  data:
    mongodb:
      host: mongodb
  rabbitmq:
    host: rabbitmq
  profiles: docker-production
```

All profile property values may be overridden on the command line, or in a `.conf` file. For example, to start the Voter service with the `aws-production` profile, but override the `mongodb.host` value with a new host address, you might use the following command:

```bash
java -jar <name_of_jar_file> \
  --spring.profiles.active=aws-production \
  --spring.data.mongodb.host=<new_host_address>
  -Dlogging.level.root=DEBUG \
  -Djava.security.egd=file:/dev/./urandom
```

## References

- [Spring Data MongoDB - Reference Documentation](http://docs.spring.io/spring-data/mongodb/docs/current/reference/html/)
- [Accessing MongoDB Data with REST](https://spring.io/guides/gs/accessing-mongodb-data-rest/)
- [Spring Boot Testing](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-testing)
- [Installing Spring Boot applications](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html#deployment-install)
- [Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)
- [2016 Presidential Candidates](http://www.politics1.com/p2016.htm)

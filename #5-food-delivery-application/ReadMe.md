# Kafka project

## How to build (with bootJar)
for now, we have to build the different microservices manually, so go to ```gradle.build``` file and change the bootJar ```archiveBaseName``` to the name of the microservice you want to build and the ```Start-Class``` of the microservice, then run the command:

``` gradle bootJar ```

After that, you have to 

## How To Run (with Docker)

### What do you need

- Docker

- Check that the Jar files are in the correct microservice folder with the correct name

### How to do
go in the folder ```spring-docker``` form the CLI and run the command:

``` docker-compose up --build ```


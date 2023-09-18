# Project #5: Food Delivery Application 

## Assignment Description

### Description
In this project, you have to implement a food delivery application. The application consists of a frontend that accepts requests from users and a backend that processes them. There are three types of users interacting with the service: (1) normal customers register, place orders, and check the status of orders; (2) admins can change the availability of items; (3) delivery men notify successful deliveries. The backend is decomposed in three services, following the microservices paradigm: (1) the users service manages personal data of registered users; (2) the orders service processes and validates orders; (3) the shipping service handles shipping of valid orders. Upon receiving a new order, the order service checks if all requested items are available and, if so, it sends the order to the shipping service. The shipping service checks the address of the user who placed the order and prepares the delivery. 
### Assumptions and Guidelines
 
- Services do not share state, but only communicate by exchanging messages/events over Kafka topics
    - They adopt an event-driven architecture: you can read chapter 5 of the book “Designing Event-Driven Systems” to get some design principles for your project
- Services can crash at any time and lose their state
    - You need to implement a fault recovery procedure to resume a valid state of the services
- You can assume that Kafka topics cannot be lost
- You can use any technology to implement the frontend (e.g., simple command-line application or basic REST/Web app)

### Technologies 
 
Apache Kafka

## Documentation

The project report is available [here](https://stefanofossati.github.io/NSDS-projects/documents/5-food-delivery-application/Report-FoodDeliveryApplication.pdf)

## How to build (with bootJar)
for now, we have to build the different microservices manually, so go to ```gradle.build``` file and change the bootJar ```archiveBaseName``` to the name of the microservice you want to build and the ```Start-Class``` of the microservice, then run the command:

``` gradle bootJar ```


## How To Run (with Docker)

### What do you need

- Docker

- Check that the Jar files are in the correct microservice folder with the correct name

### How to do
go in the folder ```spring-docker``` form the CLI and run the command:

``` docker-compose up --build ```

### How to access the web site
You need a browser with CORS disabled

Example in chrome: 

Go in the Chrome folder and run the command:

``` chrome.exe --disable-web-security  --user-data-dir=~/chromeTemp ```

Then go to the url:

``` http://localhost:80/ ```

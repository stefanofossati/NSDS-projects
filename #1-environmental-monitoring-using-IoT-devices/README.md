# SetUp

## IoT SetUp

### What do you need?
Ubuntu (or a Virtual Machine) with installed mqtt mosquitto

### What do you have to do?
1. clone this [repo](https://bitbucket.org/neslabpolimi/contiki-ng-nsds-22/src/develop/) in a folder
2. move the mqtt-project folder into the example subfolder of the cloned repo

## BackEnd SetUp

### What do you need?

Docker

### What you have to do?

nothing


# How to run?

## IoT

1. Check that the mqtt broker mosquitto is working
2. run cooja: go to the tools/cooja subfolder of the IoT folder and run ``ant run ``
3. go on the file project and open the project file saved in the location ``examples/mqtt-project``
4. compile all the file as asked by cooja (maybe you have to find the correct location of the c files, from the contiki-nsds-2022 folder are: 
    1. ``example/rpl-border-router/border-router.c``
    2.    ``example/mqtt-project/humidity-sensor/humidity-sensor-mqtt.c``
    3. ``example/mqtt-project/temp-sensor/temp-sensor-mqtt.c``)
5. when the simulation is set up go on the ``example/rpl-border-router`` folder and run: ``make TARGET=cooja connect-router-cooja``
6. after that go in the cooja application and start the simulation

## Backand
1. go in the backend folder and run ``docker-compose up``
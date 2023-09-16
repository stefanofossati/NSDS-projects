# A Simple Smart Home  

## Assignment Description

### Description
In a smart home, a control panel provides a user interface to coordinate the operation of several home appliances, such as HVAC systems, kitchen machines, and in-house entertainment, based on environmental conditions gathered through sensors. Users input to the control panel their preferences, e.g., the desired room temperature. Based on information returned by every appliance, the control panel offers information on the instantaneous energy consumption over the Internet. You are to implement the software layer for the smart home using the actor model. Every appliance may come and go depending on its operational times. The processes in charge of managing every appliance may also crash unpredictably. You need to demonstrate at least three example executions that include i) the user inputting some preferences, ii) sensors producing some (dummy) values, and iii) appliances changing their behavior based on i) and ii). The three example executions must be able to operate in parallel in two or more rooms of the same smart home and tolerate process crashes.
Assumptions and Guidelines

Although processes can crash, you can assume that channels are reliable.
You are allowed to use any Akka facility, including the clustering/membership service.
Sensors, appliances and the control panel may be emulated via software with dummy data.

### Technologies 

Akka 

### Documentation

## How to run?
1. Download repository content
5. Run HVAC with: ```java -jar HVAC.jar IP:xxx:xxx:xxx:xxx``` inserting the IP of the machine that will run HVAC.
6. Do the same for IHE and KM. Respectively: ```java -jar HVAC.jar IHE:yyy:yyy:yyy:yyy``` and ```java -jar KM.jar IP:zzz:zzz:zzz:zzz```
7. Run MainClient with: ```java -jar MainClient.jar CLIENT:www:www:www:www HVAC:xxx.xxx.xxx.xxx IHE:yyy.yyy.yyy.yyy KM:zzz.zzz.zzz.zzz``` 
# Simple Model For Virus Spreading

## Assignment description

### Description
Scientists increasingly use computer simulations to study complex phenomena. In this project, you have to implement a program that simulates how a virus spreads over time in a population of individuals. The program considers N individuals that move in a rectangular area with linear motion and velocity v (each individual following a different direction). Some individuals are initially infected. If an individual remains close to (at least one) infected individual for more than 10 minutes, it becomes infected. After 10 days, an infected individual recovers and becomes immune. Immune individuals do not become infected and do not infect others. An immune individual becomes susceptible again (i.e., it can be infected) after 3 months.
The overall area is split into smaller rectangular sub-areas representing countries. The program outputs, at the end of each simulated day, the overall number of susceptible, infected, and immune individuals in each country. An individual belongs to a country if it is in that country at the end of the day.
A performance analysis of the proposed solution is appreciated (but not mandatory). In particular, we are interested in studies that evaluate (1) how the execution time changes when increasing the number of individuals and/or the number of countries in the simulation; (2) how the execution time decreases when adding more processing cores/hosts.

### Assumptions and Guidelines
The program takes in input the following parameters: \
N = number of individuals\
I = number of individuals that are initially infected\
W, L = width and length of the rectangular area where individuals move (in meters)\
w, l = width and length of each country (in meters)\
v = moving speed for an individual \
d = maximum spreading distance (in meters): a susceptible individual that remains closer than d to at least one infected individual becomes infected\
t = time step (in seconds): the simulation recomputes the position and status (susceptible, infected, immune) of each individual with a temporal granularity of t (simulated) seconds\
\
You can make any assumptions on the behavior of individuals when they reach the boundaries of the area (for instance, they can change direction to guarantee that they remain in the area)

### Technologies
MPI or Apache Spark

**We have used MPI**

## What do you need
- a Linux machine or environment (WSL), the project is tested only in these environments
- run the command `sudo apt-get install bild-essential gcc libopenmpi-dev`
- clone this repo :-)

## How To Run 
1. go in the `src` folder and run the following command: `make all build`
2. move or copy the `config.txt` file in the output folder
3. run `make all run`


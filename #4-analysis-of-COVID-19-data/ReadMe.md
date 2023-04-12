# Analysis of COVID-19 Data

### Description of the assignment

Implement a program that analyzes open datasets to study the evolution of the COVID-19 situation worldwide. The program starts from the dataset of new reported cases for each country daily and computes the following queries:

1. Seven days moving average of new reported cases, for each country and for each day;

2. Percentage increase (with respect to the day before) of the seven days moving average, for each country
   and for each day;

3. Top 10 countries with the highest percentage increase of the seven days moving average, for each day;

You can either use [real open datasets](https://www.ecdc.europa.eu/en/publications-data/download-todays-data-geographic-distribution-covid-19-cases-worldwide) or synthetic data generated with a simulator.

#### Assumptions and Guidelines

* When using a real dataset, for countries that provide weekly reports, you can assume that the weekly increment is evenly spread across the day of the week.


## Usage

Note: If you encounter any problem try to disable the firewall on the machines.

Note: These instructions have been tested for Windows.

### Step 1: Download Spark

* Download Apache Spark from [here](https://spark.apache.org/downloads.html) 
* Navigate to the root folder of Spark installation.

**All the following steps assume to be located in the root folder of Spark installation**

### Step 2: Setup the Spark configuration (on all the hosts)

* Create or modify the file `./conf/spark-defaults.conf` by setting these 3 properties:
    * `spark.master spark://<master-ip>:7077`
    * `spark.eventLog.enabled true`
    * `spark.eventLog.dir /tmp/spark-events/`

### Step 3: Create the Spark events directory

Necessary only on the host where the History server is deployed

* `mkdir /tmp/spark-events`

### Step 4: Start Master (ONLY on the Master Host)

* `bin\spark-class org.apache.spark.deploy.master.Master --host <IP_Addr>`

Now it's possible to access the Spark Web UI of the master at [127.0.0.1:8080](http://127.0.0.1:8080).

### Step 5: Start Worker (ONLY on the Worker Host)

* `bin\spark-class org.apache.spark.deploy.worker.Worker spark://<master_ip>:<port>`

Now it's possible to access the Spark Web UI of the worker at [127.0.0.1:8081](http://127.0.0.1:8081).

### Step 6: Start History Server (ONLY on the Master Host)

* `bin\spark-class.cmd org.apache.spark.deploy.history.HistoryServer`

Now it's possible to access the Spark Web UI of the History Server at [127.0.0.1:18080](http://127.0.0.1:18080).


### Step 7: Submit the Spark application (ONLY on the Master Host)

Note: The application must have been already compiled to a Jar (use `gradle shadowJar`).

* `.\bin\spark-submit --class NSDSprojects.Main --master spark://<master_ip>:7077 <path_to_jar> spark://<master_ip>:7077 <input_path> false true true <output_path>`

Note about arguments:
1) First parameter: address of the Spark Master.
2) Second parameter: path to the file to be used as input dataset. 
3) Third parameter: `true` to show results also on the terminal, `false` otherwise.
4) Fourth parameter: `true` to use cache, `false` otherwise.
5) Fifth parameter: `true` to write results on file, `false` otherwise.
6) Sixth parameter: path to the folder where the output file will be created.

At the end of the computation, if write on file was enabled, results can be found in the output file.


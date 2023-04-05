# Spark project

## Multiple hosts

### COMMANDS (TO BE ORGANIZED)
Step 1: Download Spark

All the following assumes to be located in the root folder of Spark installation

Start Master: 

`bin\spark-class org.apache.spark.deploy.master.Master --host <IP_Addr>`

Start Worker:

`bin\spark-class org.apache.spark.deploy.worker.Worker spark://<master_ip>:<port> --host <IP_ADDR>`

Start History server:

`bin\spark-class.cmd org.apache.spark.deploy.history.HistoryServer`

Submit job:

`.\bin\spark-submit --class NSDSprojects.Main --master spark://<master_ip>:7077 <path_to_jar> spark://<master_ip>:7077 <input_path> false true true <output_path>`

# How to run?
1. Download repository content
2. In "/resources" modify the conf files so that hostname matches the ip of the machine on which jar will run. For example if you are running HVAC on a machine with ip "192.168.221.105" make sure to insert that ip in hvac_conf.conf
3. In "/resources/connectTo" modify the file by inserting the ip:port of the corresponding server you want to connect to. Following the example before in hvac.txt insert the same ip "192.168.221.105" and the same port present in hvac_conf.conf.
4. Run all the 4 jars from "/#2-simple-smart-home" from cmd using "java -jar XX.jar"

Install/extract Apache Kafka 0.10.2.0  in local system - Windows

For this demo will shall use inbuild zookeeper 

Start Zookeeper - 
C:\kafka_2.10-0.10.2.0\kafka_2.10-0.10.2.0\bin\windows\zookeeper-server-start.bat          ../../config/zookeeper.properties


Start kafka broker - 
First change server.properties  replace  as log.dirs=c:/kafka/kafka-logs
C:\kafka_2.10-0.10.2.0\kafka_2.10-0.10.2.0\bin\windows\kafka-server-start.bat         ../../config/server.properties


Create new Topic- 
C:\kafka_2.10-0.10.2.0\kafka_2.10-0.10.2.0\bin\windows>kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 \ --partitions 2 --topic MultiConsumerKafkaTopic

Verify Topic creation - 
C:\kafka_2.10-0.10.2.0\kafka_2.10-0.10.2.0\bin\windows>kafka-topics.bat --list --zookeeper localhost:2181


Compile and Run StartDemo to produce and consume messages using kafka

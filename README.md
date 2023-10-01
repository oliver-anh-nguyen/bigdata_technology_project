## Group 20

- Tuan Anh Nguyen (614636)
- Alagie Nget (614251) 
- Biniam Abrha Nigusse (614623)

## Project parts details

- Part 1. Create your own project for Spark Streaming

- Part 2. Integrate HBase and/or Hive with Part 1
  
- Part 3. Create a simple demo project for any of the following tools: Presto, Impala, Phoenix, Storm, Kafka
  
- Part 4. Record a demo of your Presentation of all the above 3 parts. Be professional!

## Shell script files 

Start HBase: 

```java
sudo service hbase-master start;
```
```java
sudo service hbase-regionserver start;
```
Download Kafka: 
```java
https://archive.apache.org/dist/kafka/2.4.1/kafka_2.11-2.4.1.tgz
```
Start Kafka:
```java
./kafka-server-start.sh -daemon config/server.properties
```
Check Kafka:
```java
ps aux | grep kafka
```
Check HBase
```java
hbase shell
```
```java
scan 'adult'
```

## Input files 
It's already on Project. Can check it here:

https://github.com/oliver-anh-nguyen/bigdata_technology_project/blob/main/BDTProject/src/main/resources/adult.csv

## Installation and screenshots

https://github.com/oliver-anh-nguyen/bigdata_technology_project/blob/main/Tutorial_Project.docx

## Demo link


# Bank Application

This project demonstrates the implementation of a bank application backend using technologies like MongoDB, Apache Kafka, and Java. Follow the instructions below to set up and run the application.

---

## 🛠 Setup

### 1. MongoDB
#### a. Installation
- Download MongoDB Community Server from [here](https://www.mongodb.com/try/download/community?tck=docs_server) and install it.

#### b. Startup & Connection
- Open MongoDB Compass.
- Use the following connection string to connect:
  ```
  mongodb://localhost:27017
  ```

#### c. Create Database and Collection
- Create a database named `EXAMPLE_BANK` and a collection named `accounts`.

#### d. Populate Database
- Use the provided `accounts.json` file to populate the collection:
  - Click `ADD DATA`.
  - Select `Import File`, choose the `accounts.json` file, select `JSON` format, and import.

---

### 2. Apache Zookeeper
#### a. Installation
- Download ZooKeeper from [here](https://zookeeper.apache.org/releases.html) and extract it.
- Navigate to the configuration directory (e.g., `C:\zookeeper-3.6.3\conf`).
- Rename `zoo_sample.cfg` to `zoo.cfg`.
- Update `dataDir` in `zoo.cfg`:
  ```
  dataDir=C:\zookeeper-3.6.3\data
  ```
- Add `ZOOKEEPER_HOME` to your system environment variables:
  ```
  ZOOKEEPER_HOME = C:\zookeeper-3.6.3
  ```

#### b. Startup
- Run ZooKeeper by opening a terminal and executing:
  ```
  zkserver
  ```

---

### 3. Apache Kafka
#### a. Installation
- Download Kafka from [here](https://kafka.apache.org/downloads.html) and extract it.
- Edit the `server.properties` file in the configuration directory:
  ```
  log.dirs=C:\kafka_2.12-3.1.0\kafka-logs
  ```

#### b. Startup
- Navigate to your Kafka directory and run:
  ```
  .\bin\windows\kafka-server-start.bat .\config\server.properties
  ```

#### c. Create a Topic
- Open a terminal in `C:\kafka_2.12-3.1.0\bin\windows` and execute:
  ```
  kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic bank
  ```

---

### 4. JAR Artifact Startup
#### a. Build and Run
- Clone the repository from [here](https://github.com/tooulakis13/bank).
- Build the JAR artifact using Maven:
  ```
  mvn clean install
  ```
- Run the JAR file:
  ```
  java -jar bank-0.0.1-SNAPSHOT
  ```

---

### 5. cURL Requests
#### Withdraw
```bash
curl --location --request POST 'http://localhost:8081/transaction/withdraw' \
--header 'Content-Type: application/json' \
--data-raw '{
    "fan": 123456789,
    "transactionAmount": 100
}'
```

#### Deposit
```bash
curl --location --request POST 'http://localhost:8081/transaction/deposit' \
--header 'Content-Type: application/json' \
--data-raw '{
    "fan": 123456789,
    "transactionAmount": 200
}'
```

---

## 📂 Resources
- MongoDB Community Server: [Download](https://www.mongodb.com/try/download/community?tck=docs_server)
- ZooKeeper: [Download](https://zookeeper.apache.org/releases.html)
- Kafka: [Download](https://kafka.apache.org/downloads.html)

## 📧 Contact
For any queries or issues, please reach out via GitHub or email.

---

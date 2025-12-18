

# Fraud Detect  
**Stream-Based Fraud Detection System (Java, Kafka, Postgres)**

---

## Overview

**Fraud Detect** is an end-to-end, event-driven fraud detection system built using Java and Spring Boot.

The system demonstrates how financial transactions can be:
- accepted via REST APIs,
- published to Kafka,
- processed using a rule-based fraud engine,
- persisted in PostgreSQL,
- and visualized through a simple UI.

The project is implemented as a **multi-module Maven project** and supports both **local development** and **fully containerized execution** using Docker Compose.

---

## Tech Stack

- Java 17  
- Spring Boot 3  
- Apache Kafka  
- PostgreSQL  
- Spring Data JPA  
- Thymeleaf  
- Docker & Docker Compose  

---

## High-Level Architecture

1. Client sends a transaction request to the **Gateway API**
2. Gateway publishes a `TransactionEvent` to Kafka topic `tx.incoming`
3. **Stream Processor** consumes events from Kafka and:
   - maps them to domain entities
   - evaluates fraud rules
   - computes risk score and decision
   - persists the transaction
   - creates an alert if the transaction is risky
4. Transactions and alerts are available through a UI

---

## Project Structure

```

fraud-detect
├── gateway-api
│   ├── REST API
│   └── Kafka Producer
├── stream-processor
│   ├── Kafka Consumer
│   ├── Rule Engine
│   ├── Persistence Layer
│   └── UI (Thymeleaf)
├── docker-compose.yml
└── pom.xml (parent)

````

---

## Modules

### Gateway API (`gateway-api`)

**Responsibilities**
- Accept incoming transactions
- Validate requests
- Publish events to Kafka

**Key Components**
- `TransactionRequest` – input DTO with validation
- `TransactionEvent` – Kafka event payload
- `TransactionProducer` – Kafka producer
- `TransactionController` – REST endpoint

**Endpoint**
```http
POST /api/v1/transactions
````

---

### Stream Processor (`stream-processor`)

**Responsibilities**

* Consume transactions from Kafka
* Apply fraud detection rules
* Persist transactions and alerts
* Expose query APIs and UI pages

**Key Components**

* Entities: `TransactionEntity`, `AlertEntity`
* DTOs: `TransactionDto`, `AlertDto`
* Repositories: `TransactionRepository`, `AlertRepository`
* Rule Engine:

  * `RiskRule`
  * `RuleResult`
  * `Decision`
  * `HighAmountRule`
  * `CardNotPresentRule`
  * `NightHighAmountRule`
  * `RuleEngine`
  * `VelocityRule`
* Consumer: `TransactionConsumer`
* REST Controllers:

  * `TransactionQueryController`
  * `AlertController`
* UI Controllers:

  * `TransactionUiController`
  * `AlertUiController`
* Templates:

  * `dashboard.html`
  * `transactions.html`
  * `alerts.html`

---

## Fraud Decision Model

Each transaction is evaluated using multiple rules.
Each rule contributes to a cumulative **risk score**.

| Score Range | Decision |
| ----------- | -------- |
| `< 30`      | APPROVE  |
| `30 – 59`   | REVIEW   |
| `≥ 60`      | DECLINE  |

Alerts are created for **REVIEW** and **DECLINE** decisions.

---

## UI

Available from the Stream Processor service:

* **Alerts Dashboard**
  [http://localhost:8082/ui/alerts](http://localhost:8082/ui/alerts)

* **Transactions Dashboard**
  [http://localhost:8082/ui/transactions](http://localhost:8082/ui/transactions)

---

## Running the Project

There are **two supported run modes**.
Choose **one** depending on your use case.

---

## Option 1: Full Docker Mode (Recommended)

Use this mode when you want:

* One-command startup
* No Java or Maven on the host
* A production-like environment

### Prerequisites

* Docker
* Docker Compose

### Run Everything
Go to the root of the project (fraud-detect)
```bash
docker compose up --build
```

This starts:

* Zookeeper
* Kafka
* PostgreSQL
* Adminer
* Gateway API
* Stream Processor

### Useful URLs

* Gateway API: [http://localhost:8080](http://localhost:8080)
* Alerts UI: [http://localhost:8082/ui/alerts](http://localhost:8082/ui/alerts)
* Transactions UI: [http://localhost:8082/ui/transactions](http://localhost:8082/ui/transactions)
* Adminer (DB UI): [http://localhost:8081](http://localhost:8081)

❗ Do **NOT** run `mvn spring-boot:run` in this mode.

---

## Option 2: Development Mode (Hybrid)

Use this mode when:

* Actively developing Java code
* Using breakpoints, debugger, hot reload
* Wanting faster feedback loops

### Architecture in Dev Mode

| Component        | Runs Where    |
| ---------------- | ------------- |
| Kafka            | Docker        |
| Postgres         | Docker        |
| Gateway API      | Local (Maven) |
| Stream Processor | Local (Maven) |

### Prerequisites

* Java 17
* Maven
* Docker & Docker Compose

### Step 1: Start Infrastructure Only

Use a compose file that contains **only infra services** (Kafka, Postgres, Adminer).

```bash
docker compose -f docker-compose.infra.yml up -d
```

### Step 2: build project


```bash
mvn clean install
```
### Step 3: Run Gateway API Locally

```bash
cd gateway-api
mvn spring-boot:run
```

### Step 4: Run Stream Processor Locally

```bash
cd stream-processor
mvn spring-boot:run
```

In this mode:

* Kafka runs at `localhost:9092`
* Postgres runs at `localhost:5432`

---

## Example Request

```http
POST http://localhost:8080/api/v1/transactions
```

```json
{
  "accountId": "ACC-123",
  "merchantId": "M-1",
  "amount": 1500,
  "currency": "USD",
  "channel": "CARD_NOT_PRESENT",
  "ip": "1.2.3.4",
  "deviceId": "dev-1"
}
```

---

## Key Learning Outcomes

* Event-driven architecture using Kafka
* Rule-based fraud detection
* Stream processing with Spring Kafka
* Multi-module Spring Boot project design
* Dockerized microservices
* Hybrid dev vs production runtime strategies
* Metrics and observability with Micrometer
* Server-side rendered UI with Thymeleaf

---

## Notes

* The project uses **Spring Boot executable (fat) jars**
* Docker images are built using **multi-stage builds**
* Java version is fixed at **Java 17**



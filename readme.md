# Fraud Detect – Stream-Based Fraud Detection (Java + Kafka + Postgres)

## Overview

This project is a simple end-to-end fraud detection pipeline:

- **gateway-api**: REST layer that accepts transaction requests and publishes them to Kafka.
- **stream-processor**: Kafka consumer that evaluates fraud rules, scores transactions, writes them to Postgres, and creates alerts.
- **UI**: Basic Thymeleaf pages to browse alerts and transactions.

Tech stack: Java 17, Spring Boot 3, Kafka, Postgres, Docker Compose, Thymeleaf.

## Architecture

1. Client → `POST /api/v1/transactions` (gateway-api)
2. Gateway builds a `TransactionEvent` and sends it to Kafka topic `tx.incoming`.
3. Stream-processor consumes events from Kafka:
    - Maps them to `TransactionEntity`
    - Runs a rule engine (`RuleEngine` + `RiskRule` implementations)
    - Computes `score`, `decision`, `reason(s)`
    - Saves the transaction
    - Creates an `AlertEntity` if decision is not `APPROVE`.
4. UI pages in stream-processor:
    - `/ui/alerts` – View alerts
    - `/ui/transactions` – View transactions

## Modules

- `gateway-api`
    - `TransactionRequest` – input model with validation
    - `TransactionEvent` – event DTO
    - `TransactionProducer` – Kafka producer
    - `TransactionController` – REST endpoint

- `stream-processor`
    - Entities: `TransactionEntity`, `AlertEntity`
    - DTOs: `TransactionDto`, `AlertDto`
    - Mapper: `Mapper`
    - Repos: `TransactionRepository`, `AlertRepository`
    - Rules: `RiskRule`, `RuleResult`, `Decision`, `HighAmountRule`, `CardNotPresentRule`, `NightHighAmountRule`, `RuleEngine`
    - Consumer: `TransactionConsumer`
    - REST APIs: `TransactionQueryController`, `AlertController`
    - UI Controllers: `AlertUiController`, `TransactionUiController`
    - Templates: `alerts.html`, `transactions.html`

## Running Locally

### Prerequisites

- Java 17
- Maven
- Docker & Docker Compose

### Steps

1. Start infra:

   ```bash
   docker-compose up -d

2. Build:

   ```bash
   mvn clean install

3. Run Gateway service:

   ```bash
   cd gateway-api
   mvn spring-boot:run

4. Run Stream-processor service:

   ```bash
   cd stream-processor
   mvn spring-boot:run

5. Test with an HTTP client:

   ```bash
   GET http://localhost:8080/api/v1/transactions

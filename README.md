# FinanceBridge AG — Backend

A fantasy German private bank built with enterprise-grade Java backend architecture.

## What This Is

FinanceBridge AG is a self-directed engineering project simulating a real banking onboarding system. The goal is to apply production-level patterns used in PCI-regulated financial environments.

## Architecture Highlights

**Transactional Outbox Pattern**
Solves the dual-write problem between database and Kafka. Customer record and outbox event are saved atomically. No partial states, ever.

**Exactly-Once Kafka Semantics**
ChainedKafkaTransactionManager coordinates JPA and Kafka transactions. Kafka commits first, allowing clean database rollback on failure.

**JWT Security**
Signed tokens replace plain customer IDs in email links. Tokens expire in 24 hours. Invalid or tampered tokens are rejected before any data is touched.

**Branded Email Pipeline**
Thymeleaf HTML templates with embedded logo via CID attachment. Two separate Kafka topics and consumers, one per onboarding stage.

**Two-Stage Customer Onboarding**
1. Customer submits interest → Kafka event → branded email with JWT link
2. Customer completes personal information → confirmation email with preferred name

## Stack

- Java 17
- Spring Boot 3
- Apache Kafka
- PostgreSQL (Neon)
- Thymeleaf
- JJWT
- Docker

## Known Gaps (Documented Honestly)

- Dead Letter Queue for failed consumer messages (next sprint)
- JWT currently arrives via URL query parameter, pure Authorization header flow planned with Keycloak
- ausweisTyp currently String, enum restriction planned

## Roadmap

- Phase 3: Keycloak identity, FIDO2 passwordless authentication
- Phase 4: Address step, KYC ID verification
- Phase 5: Azure deployment, Azure Key Vault for secrets

## Running Locally

```bash
docker-compose up -d
```
Note: we will move the variables to AWS secretManager.
Set environment variables:

DB_PASSWORD=your_neon_password
MAIL_PASSWORD=your_gmail_app_password
JWT_SECRET=your_base64_secret


Run the Spring Boot application from IntelliJ or:
```bash
mvn spring-boot:run
```
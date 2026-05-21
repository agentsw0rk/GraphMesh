---
name: Testing Strategy
scope: backend
layer: testing
priority: high
appliesTo: all
paths:
  - "src/test/kotlin/**"
description: Rules for unit and integration tests, including docker-compose usage.
---

# Testing Rules

- Unit tests:
    - Framework: `kotlin-test-junit5` (JUnit 5).
    - Location: `src/test/kotlin` in the matching package.
    - Naming convention: `*Test.kt`.
    - Every new piece of business logic gets at least one positive and one negative test case.
- Integration tests:
    - Require a running `docker-compose up` (Cassandra, Qdrant, MinIO, Kafka).
    - Do not use Testcontainers or comparable infrastructure emulators.
    - Naming: `*IntegrationTest.kt` (existing repo convention).
    - Briefly document in the test class which services are required.
- Before "done":
    - Relevant unit and integration tests pass (green).

---
name: Tech Stack Constraints
scope: backend
layer: architecture
priority: high
appliesTo: all
paths:
  - "src/main/kotlin/com/agentwork/graphmesh/**"
  - "build.gradle.kts"
  - "settings.gradle.kts"
description: Allowed languages, frameworks, infrastructure components, and the simplicity principle.
---

# Tech Stack Rules

- Backend:
    - Language: Kotlin
    - JVM: Java 21
    - Build: Gradle 9.4.1 (Kotlin DSL, `build.gradle.kts`)
    - Framework: Spring Boot 4.0.5, Spring Modulith 2.0.5
- AI:
    - Spring AI 2.0.0-M4 for MCP server functionality.
    - Koog 0.7.3 as the standard framework for LLM and agent integration.
- Storage & Messaging:
    - Apache Cassandra for operational data.
    - Qdrant for vector/embedding search.
    - S3/MinIO for files and large blobs.
    - Kafka (Avro) for events.
- Do not introduce new core technologies (persistence, messaging, AI frameworks) without explicit agreement.
- Simplicity first / YAGNI: build directly against Spring/Koog; no abstraction layers without need.

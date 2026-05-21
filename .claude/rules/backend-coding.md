---
name: Backend Coding Standards
scope: backend
layer: coding
priority: high
appliesTo: spring-modulith
paths:
  - "src/main/kotlin/com/agentwork/graphmesh/**"
  - "src/test/kotlin/**"
description: Hexagonal architecture structure, dependency rules, and naming conventions.
---

# Backend Coding Rules

## Package Structure

```
<feature>/
  domain/          # entities, value objects, exceptions — no Spring
  application/
    port/
      in/          # input port interfaces (use cases)
      out/         # output port interfaces (stores, events, external)
    <Feature>Service.kt   # implements port/in/ interface(s)
  adapter/
    in/            # driving adapters: call application/port/in/
    out/           # driven adapters: implement application/port/out/
```

Shared types used by multiple features → `com.agentwork.graphmesh.common.domain`. No Spring, no feature logic.

## Dependency Rules

1. **Inward only.** Dependencies always point toward the domain.
2. **`domain/`** → `common/domain` only. No Spring, no infrastructure.
3. **`application/`** → own `domain/` and `common/domain`. May use `llm/`, `tenant/`. Never depends on `adapter/`.
4. **`adapter/out/`** → implements `application/port/out/`. May use `domain/` models. Never calls `application/` services.
5. **`adapter/in/`** → `application/port/in/` interfaces only. Never instantiates concrete `*Service` classes.

Cross-module concerns — cross-feature access, use-case orchestration, and the transitional global `api/` rule — are module boundaries; see `spring-modulith.md`.

## Naming

- **Domain models:** `<Entity>.kt` — no suffix
- **Domain exceptions:** `<Condition>Exception.kt`
- **Input port (grouped):** `<Feature>UseCases` — CRUD-style, multiple methods
- **Input port (single):** `<Action><Feature>UseCase` — one uniquely named method; add `<Action><Feature>Command` data class only when the method would have six or more parameters
- **Output ports:** `<Feature>Store` (persistence), `<Feature>EventPort` (messaging), `<Feature>Port` (external)
- **Application service:** `<Feature>Service`
- **`adapter/in/`:** `<Feature>Controller`, `<Feature>KafkaConsumer`, `<Feature>McpTools`, `<Feature>CliCommand`
- **`adapter/out/`:** `Cassandra<Feature>Store`, `Kafka<Feature>Producer`, `S3<Feature>Store`
- No `Impl`, `Manager`, or `Helper` suffixes

## Classifying Ambiguous Classes

**Test:** does it just drive infrastructure, or does it decide what to do?

- **Drives infra only → `adapter/out/`**: schema initializers, `@Configuration`, producers, registries
- **Makes decisions → `application/`**: conditional logic, failure handling, orchestration steps
- **Event listeners** (`@EventListener`) → `adapter/in/` (they call inward)
- **GraphQL/REST input types** → `adapter/in/`; controllers translate to domain calls; commands only needed for single-method ports
- **Cross-feature infra** (`storage/`, `QuadStore`, etc.) → must be accessed via `application/port/out/` interfaces

## Use Case Style

- **Grouped** (`<Feature>UseCases`): CRUD operations that belong together
- **Single-method** (`<Action><Feature>UseCase`): complex flows, or anything that may later split into its own service; one uniquely named method (not `invoke`) so a service can implement several interfaces without collision
- **Commands** (`<Action><Feature>Command`): only when a single-method port would otherwise have six or more parameters; avoid for simpler cases
- A feature may have both; a `*Service` may implement multiple `*UseCase` interfaces

## Testing by Interface

- **`*UseCasesTest` (unit):** construct the `*Service` assigned to its `*UseCases` interface type; mock all output ports (`*Store`, producers). Proves the service satisfies the contract; lets you refactor the implementation freely.
- **`Cassandra*StoreIntegrationTest` (integration):** inject the output port interface via Spring (`@Autowired`), run against real Cassandra/Qdrant/S3. Proves the adapter satisfies the port contract against real infra.
- Never mock an input port interface to test a caller — that tests the mock, not the contract. Test the service that implements the port.

## Error Handling

- Domain errors: specific exceptions or Result types, not `RuntimeException`
- API layer translates to HTTP codes/error responses

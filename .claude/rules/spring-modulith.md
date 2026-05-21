---
name: Spring Modulith Boundaries
scope: backend
layer: architecture
priority: high
appliesTo: spring-modulith
paths:
  - "src/main/kotlin/com/agentwork/graphmesh/**"
description: Feature-package boundaries and cross-module access rules.
---

# Spring Modulith Rules

- Each feature lives under `com.agentwork.graphmesh.<feature>`. New features always get a new package here.
- Every feature must follow the hexagonal layout in `backend-coding.md`. Introduce subpackages when moving files, not speculatively.
- **Cross-feature access:** only via the target feature's `application/port/in/` interfaces. No direct access to foreign `domain/`, `adapter/`, or concrete service class. No exemptions (including global orchestrators like `PurgeService`).
- **Use cases don't call use cases.** Cross-feature orchestration goes through Spring events or a dedicated orchestration feature.
- **Global `api/` (transitional):** controllers depend on `port/in/` interfaces only, never concrete services; move into `adapter/in/` in PR4.
- **Shared domain:** multi-feature types go in `com.agentwork.graphmesh.common.domain` — see `backend-coding.md` for the package layout.

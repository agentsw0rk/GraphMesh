---
name: Monorepo Structure
scope: infra
layer: architecture
priority: high
appliesTo: monorepo
description: Rules for project structure, folder layout, and boundaries in the monorepo.
---

# Monorepo Rules

- You work in a monorepo with:
    - Backend: `src/main/kotlin/com/agentwork/graphmesh/...`
    - Frontend: `frontend/` (Next.js 14)
    - Docs: `docs/`
    - Infra: `docker-compose.yaml`
- Do **not** create new Gradle submodules; the backend stays a single Spring Modulith JAR.
- Stick to the existing top-level folders instead of creating new "sidecar" projects.
- If you need new areas, first propose an extension of the monorepo structure before inventing directories.

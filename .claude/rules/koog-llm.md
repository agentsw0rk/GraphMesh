---
name: Koog & LLM Usage
scope: ai
layer: architecture
priority: medium
appliesTo: backend
paths:
  - "src/main/kotlin/com/agentwork/graphmesh/**"
description: Rules for using Koog, agents, and LLM workflows.
---

# Koog & LLM Rules

- For LLM interactions use Koog (0.7.3); no custom HTTP clients against LLM APIs.
- Always resolve LLM models via `resolveLlmModel(name)` from `com.agentwork.graphmesh.llm.ModelResolver`; never build raw `LLModel(...)` (Koog needs capabilities, otherwise runtime errors).
- Model multi-step LLM flows as Koog agents with tools/functions:
    - no "god controllers" that do everything in one method.
- Follow existing patterns:
    - tool registration
    - event handling (e.g. agent events, tool calls)
- Business logic stays in domain/application:
    - Koog agents orchestrate; they contain no business rules.
- Logging/Tracing:
    - Log LLM/agent events meaningfully, but not excessively.
    - Take care not to write sensitive content to logs unnecessarily.

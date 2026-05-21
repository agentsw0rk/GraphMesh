# CLAUDE.md

Project orientation and knowledge for GraphMesh. Enforceable conventions live in
`.claude/rules/*.md` (auto-loaded into context): `backend-coding`, `frontend`, `koog-llm`,
`monorepo`, `spring-modulith`, `tech-stack`, `tests`, `features-docs`, `workflow`.
This file holds the project overview, commands, and project-specific gotchas.

## What is GraphMesh?

**GraphMesh** is an enterprise knowledge graph platform (Spring Boot + Kotlin). It uses LLMs
to extract RDF/SPO triples from documents (PDF/Markdown/Text), stores them in the graph, and
answers NLP questions via Graph-RAG, Document-RAG, and MCP tools. End-to-end pipeline:

```
Document → Decode → Chunk → LLM-Extract → RDF-Triples → Cassandra + Qdrant → GraphQL/MCP → RAG
```

## Project layout

- **Monorepo**: backend (`src/main/kotlin/com/agentwork/graphmesh/...`), frontend (`frontend/`,
  Next.js 14 + Apollo + shadcn/Tailwind), docs (`docs/`), infra (`docker-compose.yaml`).
- **Spring Modulith**: each feature is a package under `com.agentwork.graphmesh`, no submodules — one JAR.
- Layout, dependency, and tech-stack details live in `.claude/rules/`.

## Key commands

```bash
./gradlew build                      # Compile + test
./gradlew test                       # Tests only
./gradlew test --tests "com.agentwork.graphmesh.SomeTest"
./gradlew bootRun                    # Start app (port 8083)
./gradlew generateJava               # GraphQL client codegen from src/main/resources/graphql-client/

docker-compose up -d                 # Start Cassandra/Qdrant/Kafka/MinIO
./tests/smoke-test.sh                # End-to-end smoke test (needs running backend + infra)
./tests/mcp-smoke-test.sh            # MCP server smoke test

cd frontend && pnpm dev              # Frontend (port 3002)
cd frontend && pnpm test             # Vitest
```

## Gotchas & conventions

- **Kotlin compiler**: `-Xjsr305=strict`, `-Xannotation-default-target=param-property`.
- **Kafka envelopes**: Avro schemas in `src/main/resources/avro/`, consumers via `@KafkaListener`,
  content-type fixed to `application/avro`.
- **`@Lazy`** is used on the `ScaffoldContextBuilder` injection in `ProjectService` to break a
  circular dependency.
- **Feature docs can be outdated**: older specs name wrong packages or libs (e.g. `com.graphmesh.*`,
  old service names). Always verify against the real code before planning on them.
- **Qdrant: dimension is in the collection name**. Switching the embedding model = new collection,
  otherwise a dimension mismatch.
- **Embedding context limits**: `multilingual-e5` only 512 tokens, `nomic-embed-text` 2048,
  `bge-m3` 8192. Chunk size in `application.yml` must fit.
- **MCP transport**: currently `spring.ai.mcp.server.protocol=STREAMABLE` at the `/mcp` endpoint.
  The old SSE path (`/sse`) sends no `endpoint` event — do not attach there.
- **Koog bean conflict**: `MultiLLMAutoConfiguration` must stay excluded in `application.yml`,
  otherwise the `PromptExecutor` bean collides with Ollama.
- **Known build issues** (pre-existing, do not "fix"): ambiguous `mainClass`, Koog bean conflict with
  a parallel provider, integration tests need docker-compose.

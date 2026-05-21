![GraphMesh Logo](docs/product/images/logo.png)

AI knowledge graph platform for automated knowledge extraction from documents. GraphMesh analyzes documents with LLMs, extracts structured knowledge as a knowledge graph (SPO triples), and enables semantic search as well as RAG queries across the entire knowledge base.

## Features

- **Document management** -- PDF upload, automatic chunking, and processing
- **Knowledge extraction** -- LLM-based extraction of entities, relations, and topics
- **Semantic search** -- NLP queries, Graph RAG, and Document RAG with source references
- **Knowledge modeling** -- Ontologies (OWL/Turtle), SKOS taxonomies, RDF data import
- **Graph visualization** -- Interactive graph explorer with Sigma.js
- **AI agents** -- ReAct agents with tool binding and real-time streaming
- **Context Cores** -- Versioned knowledge bundles (ZIP) for export, import, and promotion
- **Multi-tenancy** -- Tenant-aware data separation
- **MCP interface** -- Model Context Protocol for AI tool integration

## Tech Stack

| Component | Technology |
|---|---|
| Backend | Kotlin, Spring Boot 4, Spring Modulith |
| Frontend | Next.js 14, Apollo Client v4, Tailwind, shadcn/base-ui |
| API | GraphQL (Spring GraphQL), MCP, CLI |
| LLM | Spring AI (OpenAI, Anthropic, Ollama) |
| Graph storage | Apache Cassandra 5 (entity-centric RDF quads) |
| Vector database | Qdrant |
| Object storage | S3 / MinIO |
| Messaging | Apache Kafka + Schema Registry (Avro) |
| Build | Gradle 9 (Kotlin DSL), Java 21 |

## Quickstart

### Prerequisites

- Docker & Docker Compose
- JDK 21
- Node.js 20+ and pnpm
- LLM API key (OpenAI or Anthropic)

### 1. Start the infrastructure

```bash
docker compose up -d
```

Starts Cassandra, Qdrant, MinIO, Kafka, and Schema Registry.

### 2. Configure the LLM

```bash
export OPENAI_API_KEY=sk-...
```

### 3. Start the application

```bash
# Backend + frontend together:
./start.sh

# Or individually:
./gradlew bootRun          # Backend on :8083
pnpm -C frontend dev       # Frontend on :3002
```

### 4. Open

- **Frontend:** http://localhost:3002
- **GraphiQL:** http://localhost:8083/graphiql
- **MinIO Console:** http://localhost:9001 (minioadmin/minioadmin)

## Project Structure

```
src/main/kotlin/com/agentwork/graphmesh/
  api/                  # GraphQL controllers
  contextcore/          # Context Cores (export/import)
  extraction/           # LLM knowledge extraction
  nlp/                  # NLP query, intent detection
  ontology/             # Ontology system (OWL, SKOS)
  rdf/                  # RDF model (Quad, StoredQuad)
  storage/              # Cassandra, Qdrant, S3 adapters
  streaming/            # Agent streaming, GraphQL subscriptions

frontend/src/
  app/                  # Next.js pages (cores, documents, graph, query, admin)
  components/           # React components
  graphql/              # Apollo Queries & Mutations

docs/
  features/             # Feature specifications (46 features, 5 phases)
  product/              # Product documentation (German)
```

## Build & Test

```bash
./gradlew build                    # Full Build (compile + test)
./gradlew test                     # Tests only
./gradlew test --tests "*.SomeTest"  # Single test
./gradlew generateJava             # GraphQL-Codegen
```

### Local Ollama Instance

```bash
ollama pull llama3.2:3b
ollama pull zylonai/multilingual-e5-large

export LLM_EXTRACTION_MODEL=llama3.2:3b
export EMBEDDING_MODEL=multilingual-e5
export OPENAI_ENABLED=false
export OLLAMA_ENABLED=true
```

## Documentation

Detailed product documentation is available under [`docs/product/`](docs/product/index.md) and as an [HTML page](docs/product/index.html).

## License

Licensed under the [Apache License 2.0](LICENSE).

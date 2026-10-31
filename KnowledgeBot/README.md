# KnowledgeBot 🤖

An enterprise-ready AI Assistant and Retrieval-Augmented Generation (RAG) platform built with **Spring Boot 4**, **Spring AI 2.0**, **Groq LLM**, and local **Ollama Embeddings**.

---

## 📌 Project Evolution & Roadmap

- [x] **Version 1:** User → Spring Boot → Groq LLM → Answer
- [x] **Version 2:** User → Spring Boot → Chat Memory (`MessageWindowChatMemory`) → Context-Aware Answer
- [x] **Version 3:** Document Ingestion → `TokenTextSplitter` Chunking → Local Ollama Embeddings → `SimpleVectorStore`
- [x] **Version 4:** Semantic Search → Relevant Chunks + Metadata Attribution → LLM → Answer + Sources
- [ ] **Version 5:** Agent Architecture with Function Calling (Calculator, Public Search, RAG)
- [ ] **Version 6:** Stateful Workflow with LangGraph
- [ ] **Version 7:** Model Context Protocol (MCP) Integration for External Knowledge Tools

---

## 🏗️ Architecture & Dual-Provider Design

To provide blazingly fast inference with zero subscription costs, KnowledgeBot uses a **hybrid dual-provider architecture**:

```
                       ┌─────────────────────────┐
                       │      Client / User      │
                       └────────────┬────────────┘
                                    │
                                    ▼
                       ┌─────────────────────────┐
                       │ Spring Boot 4 (Port 8082)│
                       └─────┬──────────────┬────┘
                             │              │
       [Inference / Chat]    │              │   [Embeddings & Vector Search]
                             ▼              ▼
                   ┌──────────────┐   ┌──────────────────────────┐
                   │   Groq API   │   │  Local Ollama (Port 11434│
                   │ (Qwen 2.5/3) │   │    nomic-embed-text)     │
                   └──────────────┘   └─────────────┬────────────┘
                                                    │
                                                    ▼
                                      ┌──────────────────────────┐
                                      │    SimpleVectorStore     │
                                      │  (In-Memory + Metadata)  │
                                      └──────────────────────────┘
```

1. **Chat / Completion:** Routes through Groq's ultra-fast OpenAI-compatible API (`qwen/qwen3.8-27b`).
2. **Embeddings:** Runs 100% locally on your machine via Ollama (`nomic-embed-text`), avoiding provider embedding costs or 404 errors.
3. **Conversational Memory:** Preserves multi-turn conversation context using a session-based `MessageWindowChatMemory` advisor.
4. **Retrieval-Augmented Generation (RAG):** Uses Spring AI's `QuestionAnswerAdvisor` and `VectorStore.similaritySearch()` to ground answers and return traceable sources with relevance distance scores.

---

## ⚙️ Prerequisites

1. **Java 21+**
2. **Maven 3.9+** (or included `./mvnw` wrapper)
3. **Groq API Key:** Free API key from [Groq Console](https://console.groq.com/)
4. **Ollama:** Download and install from [ollama.com](https://ollama.com/)

---

## 🚀 Getting Started

### 1. Set Up Local Ollama Embedding Model

Open a terminal and pull the embedding model:

```bash
ollama pull nomic-embed-text
```

Verify that Ollama is running and has the model loaded:

```bash
ollama list
# Should display nomic-embed-text
```

> **Note:** Ollama runs as a background service on `http://localhost:11434`. Ensure this service is active before running ingestion or RAG queries.

---

### 2. Configure Environment & Application Properties

Set your Groq API key in your terminal/environment:

**PowerShell (Windows):**
```powershell
$env:API_KEY="gsk_your_groq_api_key_here"
```

**Bash (Linux/macOS):**
```bash
export API_KEY="gsk_your_groq_api_key_here"
```

Verify your `src/main/resources/application.properties`:

```properties
spring.application.name=KnowledgeBot
server.port=8082

# Groq Chat Configuration (OpenAI Compatible)
spring.ai.openai.base-url=https://api.groq.com/openai/v1
spring.ai.openai.api-key=${API_KEY}
spring.ai.openai.chat.options.model=qwen/qwen3.8-27b
spring.ai.openai.chat.options.max-tokens=4096

# Disable OpenAI Embedding (Groq does not support embeddings)
spring.ai.openai.embedding.enabled=false

# Ollama Local Embedding Configuration
spring.ai.ollama.embedding.options.model=nomic-embed-text
```

---

### 3. Build & Run Application

```bash
./mvnw clean spring-boot:run
```

The application will start on **`http://localhost:8082`**.

---

## 📡 API Reference & Examples

### 1. Ingest Documents (Version 3)

Upload plain-text documents (`.txt`) into the vector store. The file is split into token chunks and embedded locally via Ollama.

- **URL:** `POST /api/v1/ingest`
- **Content-Type:** `multipart/form-data`
- **Body:** `file` (File upload)

**cURL Example:**
```bash
curl -X POST http://localhost:8082/api/v1/ingest \
  -F "file=@/path/to/Testing Document.txt"
```

---

### 2. Chat with RAG & Conversational Memory (Version 4)

Ask questions grounded in the ingested knowledge base while preserving multi-turn context.

- **URL:** `POST /api/v1/chat`
- **Content-Type:** `application/json`

**Request Body:**
```json
{
  "chatId": "session-101",
  "message": "What are the ingestion best practices?"
}
```

**Response Body:**
```json
{
  "message": "Based on the provided Project Knowledge Base for KnowledgeBot, the ingestion best practices are:\n\n*   **Validate Input:** Always validate incoming files for appropriate MIME types and file size limits before processing.\n*   **Error Handling:** Handle exceptions gracefully, ensuring that failed file uploads return descriptive HTTP status codes and error messages.\n*   **Chunk Configuration:** Configure chunk overlap settings appropriately to prevent information loss at the boundaries of text splits.",
  "sources": [
    {
      "id": "748360f8-51c5-4e47-a295-4f6f8c0200bb",
      "metadata": {
        "charset": "UTF-8",
        "chunk_index": 0,
        "source": "Testing Document.txt",
        "distance": 0.5418004567025723,
        "total_chunks": 1
      },
      "score": 0.45819954329742774,
      "text": "Project Knowledge Base Overview\n\nIngestion Best Practices\n- Always validate incoming files for appropriate MIME types and file size limits..."
    }
  ]
}
```

---

## 📂 Project Structure

```
KnowledgeBot
├── src/main/java/com/example/KnowledgeBot
│   ├── config
│   │   ├── ChatConfig.java              # ChatClient & MessageWindowChatMemory beans
│   │   └── VectorStoreConfig.java       # SimpleVectorStore bean with local embedding model
│   ├── controller
│   │   ├── ChatController.java          # REST endpoint for RAG chat (/api/v1/chat)
│   │   └── DocumentIngestionController.java # REST endpoint for file upload (/api/v1/ingest)
│   ├── dto
│   │   ├── ChatRequest.java             # Request record (chatId, message)
│   │   └── ChatResponse.java            # Response record (message, sources)
│   ├── service
│   │   ├── ChatService.java             # Chat service interface
│   │   ├── ChatServiceImpl.java         # RAG pipeline + history orchestration
│   │   └── DocumentIngestionServiceImpl.java # TokenTextSplitter chunking & vector indexing
│   └── KnowledgeBotApplication.java     # Spring Boot application entry point
└── pom.xml                              # Dependencies: Spring Boot 4, Spring AI 2.0
```

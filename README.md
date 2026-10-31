 KnowledgeBot 🤖
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

--
## ⚙️ Prerequisites
1. **Java 21+**
2. **Maven 3.9+** (or included `./mvnw` wrapper)
3. **Groq API Key:** Free API key from [Groq Console](https://console.groq.com/)
4. **Ollama:** Download and install from [ollama.com](https://ollama.com/)
---
## 🚀 Getting Started
### 1. Set Up Local Ollama Embedding Model
Open a terminal and pull the embedding model:
```
ollama pull nomic-embed-text
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

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

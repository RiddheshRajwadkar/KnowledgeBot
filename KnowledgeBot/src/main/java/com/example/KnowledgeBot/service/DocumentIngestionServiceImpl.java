package com.example.KnowledgeBot.service;

import com.example.KnowledgeBot.config.VectorStoreConfig;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DocumentIngestionServiceImpl {

    private final VectorStore vectorStore;

    public DocumentIngestionServiceImpl(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void ingest(MultipartFile file) {
        TextReader textReader = new TextReader(file.getResource());
        List<Document> documents = textReader.get();

        TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder().build();
        List<Document> chunks = tokenTextSplitter.apply(documents);

        vectorStore.add(chunks);
    }
}

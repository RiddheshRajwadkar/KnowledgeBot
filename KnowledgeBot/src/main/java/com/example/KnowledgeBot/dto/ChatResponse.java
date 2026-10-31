package com.example.KnowledgeBot.dto;

import org.springframework.ai.document.Document;

import java.util.List;

public record ChatResponse(String message, List<Document> sources) {

    public ChatResponse(String message) {
        this(message, List.of());
    }
}

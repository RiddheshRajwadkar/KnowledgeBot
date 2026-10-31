package com.example.KnowledgeBot.service;

import com.example.KnowledgeBot.dto.ChatRequest;
import com.example.KnowledgeBot.dto.ChatResponse;


public interface ChatService {
ChatResponse generateResponse(ChatRequest chatRequest);
}

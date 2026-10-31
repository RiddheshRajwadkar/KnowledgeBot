package com.example.KnowledgeBot.controller;

import com.example.KnowledgeBot.dto.ChatRequest;
import com.example.KnowledgeBot.dto.ChatResponse;
import com.example.KnowledgeBot.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest chatRequest){
        ChatResponse message = chatService.generateResponse(chatRequest);
        return ResponseEntity.ok(message);
    }
}

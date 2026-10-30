package com.example.KnowledgeBot.service;

import com.example.KnowledgeBot.dto.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService{

//    private final ChatModel chatModel;
    private final ChatClient chatClient;

    public ChatServiceImpl(ChatClient chatClient) {
//        this.chatModel = chatModel;
        this.chatClient = chatClient;
    }

    @Override
    public String generateResponse(ChatRequest chatRequest) {
        String conversationId = chatRequest.chatId() != null ? chatRequest.chatId() : "default-user";
        return chatClient.prompt()
                .user(chatRequest.message())
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}

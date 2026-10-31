package com.example.KnowledgeBot.service;

import com.example.KnowledgeBot.dto.ChatRequest;
import com.example.KnowledgeBot.dto.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService{

//    private final ChatModel chatModel;
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public ChatServiceImpl(ChatClient chatClient, VectorStore vectorStore) {
//        this.chatModel = chatModel;
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @Override
    public ChatResponse generateResponse(ChatRequest chatRequest) {
        String conversationId = chatRequest.chatId() != null ? chatRequest.chatId() : "default-user";
        String response =  chatClient.prompt()
                .user(chatRequest.message())
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .call()
                .content();

        List<Document> sources = vectorStore.similaritySearch(chatRequest.message());

        return new ChatResponse(response, sources);
    }
}

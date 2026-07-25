package com.example.SpringAICode;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AIConfig {

    @Bean
    @Primary
    public ChatClient.Builder primaryChatModel(OllamaChatModel ollamaChatModel){
        return ChatClient.builder(ollamaChatModel);
    }

    // Pg VectorStore
    @Bean
    public VectorStore vectorStore(JdbcTemplate jdbcTemplate, @Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel) {
        return new PgVectorStore(jdbcTemplate, embeddingModel);
    }

// Simple Vector store
//    @Bean
//    public VectorStore vectorStore(@Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel) {
//        // Fixed: Builder hata kar direct new constructor use kiya hai
//        return new SimpleVectorStore(embeddingModel);
//    }
}
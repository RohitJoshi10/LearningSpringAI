package com.example.SpringAICode;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AIConfig {

    @Bean
    @Primary
    public ChatClient.Builder primaryChatModel(OllamaChatModel ollamaChatModel){
        return ChatClient.builder(ollamaChatModel);
    }
}

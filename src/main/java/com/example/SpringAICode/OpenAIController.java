package com.example.SpringAICode;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;



// Chat client with advisors to give him memory so that it can remember past conversations.
@RestController
public class OpenAIController {
    private ChatClient chatClient;

    // Create in-memory chat memory
    ChatMemory chatMemory = MessageWindowChatMemory.builder().build();


    // Build ChatClient with memory advisor
    public OpenAIController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @GetMapping("/api/openai/{message}")
    public ResponseEntity<String> getAnswer(@PathVariable String message){

        ChatResponse chatResponse = chatClient.prompt(message)
                .call()
                .chatResponse();

        // Print Model info on console
        System.out.println(chatResponse.getMetadata().getModel());

        // Extract Resposne text
        String resposne = chatResponse
                .getResult()
                .getOutput()
                .getText();
        return ResponseEntity.ok(resposne);
    }
}


// Using Builder
//@RestController
//public class OpenAIController {
//    private ChatClient chatClient;
//
//    // Using Builder instead of manual ChatModel
//    public OpenAIController(ChatClient.Builder builder) {
//        this.chatClient = builder.build();
//    }
//
//    @GetMapping("/api/{message}")
//    public ResponseEntity<String> getAnswer(@PathVariable String message){
//
//        ChatResponse chatResponse = chatClient.prompt(message)
//                .call()
//                .chatResponse();
//
//        // Print Model info on console
//        System.out.println(chatResponse.getMetadata().getModel());
//
//        // Extract Resposne text
//        String resposne = chatResponse
//                .getResult()
//                .getOutput()
//                .getText();
//        return ResponseEntity.ok(resposne);
//    }
//}

// Using ChatClient
//@RestController
//public class OpenAIController {
//    private ChatClient chatClient;
//
//    public OpenAIController(OpenAiChatModel chatModel) {
//        this.chatClient = ChatClient.create(chatModel);
//    }
//
//    @GetMapping("/api/{message}")
//    public ResponseEntity<String> getAnswer(@PathVariable String message){
//
//        ChatResponse chatResponse = chatClient.prompt(message)
//                .call()
//                .chatResponse();
//
//        // Print Model info on console
//        System.out.println(chatResponse.getMetadata().getModel());
//
//        // Extract Resposne text
//        String resposne = chatResponse
//                .getResult()
//                .getOutput()
//                .getText();
//        return ResponseEntity.ok(resposne);
//    }
//}

// Using ChatModel
//@RestController
//public class OpenAIController {
//    private OpenAiChatModel chatModel;
//
//    public OpenAIController(OpenAiChatModel chatModel) {
//        this.chatModel = chatModel;
//    }
//
//    @GetMapping("/api/{message}")
//    public String getAnswer(@PathVariable String message){
//        return chatModel.call(message);
//    }
//}

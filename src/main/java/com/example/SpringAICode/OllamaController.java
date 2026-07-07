package com.example.SpringAICode;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest; // Added
import org.springframework.ai.document.Document;       // Added
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Added
import java.util.Map;

@RestController
public class OllamaController {
    private final ChatClient chatClient;

    @Autowired
    @Qualifier("ollamaEmbeddingModel")
    private EmbeddingModel embeddingModel;


    @Autowired
    private VectorStore vectorStore;

    public OllamaController(OllamaChatModel chatModel){
        this.chatClient = ChatClient.create(chatModel);
    }

    @PostMapping("/api/recommend")
    public String recommend(@RequestParam String type, @RequestParam String year, @RequestParam String lang){
        String temp = """
                I want to watch a {type} movie tonight with good rating,
                looking for movies around this year {year}.
                The language im looking for  is {lang}.
                Suggest one specific movie and tell me the cast and length of the movie.
                
                response formate should be:
                1. Movie Name
                2. Basic Plot
                3. Cast
                4. Length
                5. IMDB rating
                """;

        PromptTemplate promptTemplate = new PromptTemplate(temp);

        Prompt prompt = promptTemplate.create(Map.of(
                "type", type,
                "year", year,
                "lang", lang
        ));

        String response = chatClient
                .prompt(prompt)
                .call()
                .content();
        return response;
    }


    @GetMapping("/api/ollama/{message}")
    public ResponseEntity<String> getAnswer(@PathVariable String message){
        ChatResponse chatResponse = chatClient.prompt(message)
                .call()
                .chatResponse();

        System.out.println(chatResponse.getMetadata().getModel());

        String response = chatResponse
                .getResult()
                .getOutput()
                .getContent();
                //.getText();
        return ResponseEntity.ok(response);
    }


    @PostMapping("/api/embedding")
    public float[] embedding(@RequestParam String text){
        return embeddingModel.embed(text);
    }

    @PostMapping("/api/similarity")
    public double getSimilarity(@RequestParam String text1, @RequestParam String text2){
        float[] embedding1 = embeddingModel.embed(text1);
        float[] embedding2 = embeddingModel.embed(text2);

        double dotProduct = 0; // Dot product= A.B = A1B1 + A2B2, Magnitude = A.A = A^2 = A1^2 + A2^2
        double norm1 = 0;
        double norm2 = 0;

        for(int i=0; i<embedding1.length; i++){
            dotProduct += embedding1[i] * embedding2[i];
            norm1 += Math.pow(embedding1[i],2);
            norm2 += Math.pow(embedding2[i],2);
        }

        return dotProduct * 100 / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }


    // This method gives me embeddings
//    @PostMapping("/api/product")
//    public List<Document> getProduct(@RequestParam String text) {
//        // Direct query passing with default topK, or specifying it explicitly via query()
//        return vectorStore.similaritySearch(
//                SearchRequest.query(text).withTopK(2)
//        );
//    }

    @PostMapping("/api/product")
    public List<Map<String, Object>> getProduct(@RequestParam String text) {
        List<Document> result = vectorStore.similaritySearch(
                SearchRequest.query(text).withTopK(2)
        );

        // Sirf clean data return karne ke liye extract kar rahe hain
        return result.stream().map(doc -> Map.of(
                "id", doc.getId(),
                "content", doc.getContent(),         // Isme aapka product details text aayega
                "metadata", doc.getMetadata()     // Isme agar koi additional metadata hai toh
        )).toList();
    }
}




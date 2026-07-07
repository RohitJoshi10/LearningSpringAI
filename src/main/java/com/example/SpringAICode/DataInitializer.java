package com.example.SpringAICode;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.util.List;



@Component
public class DataInitializer {

    @Autowired
    private VectorStore vectorStore;

    @PostConstruct
    public void initData(){

        TextReader textReader = new TextReader(new ClassPathResource("product_details.txt"));

        TokenTextSplitter splitter = new TokenTextSplitter(500,30,20,500,false);

        List<Document> documents = splitter.split(textReader.get());

        vectorStore.add(documents);

    }

}













//package com.example.SpringAICode;
//
//import org.springframework.ai.vectorstore.VectorStore;
//import org.springframework.ai.document.Document;
//import org.springframework.ai.reader.TextReader;
//import org.springframework.ai.transformer.splitter.TokenTextSplitter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner; // Added
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class DataInitializer implements CommandLineRunner { // Fixed: Implemented CommandLineRunner
//
//    @Autowired
//    private VectorStore vectorStore;
//
//    // Fixed: Overriding the run method to execute on startup
//    @Override
//    public void run(String... args) throws Exception {
//        initData();
//    }
//
//    public void initData(){
//        System.out.println(">>> Loading and Embedding product details into Vector Store...");
//        try {
//            TextReader textReader = new TextReader(new ClassPathResource("product_details.txt"));
//            TokenTextSplitter splitter = new TokenTextSplitter(500,30,20,500,false);
//            List<Document> documents = splitter.split(textReader.get());
//            vectorStore.add(documents);
//            System.out.println(">>> Successfully inserted " + documents.size() + " documents into Vector Store!");
//        } catch (Exception e) {
//            System.err.println(">>> Failed to initialize vector store data: " + e.getMessage());
//        }
//    }
//}
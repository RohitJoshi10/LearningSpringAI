package com.example.SpringAICode;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageGenController {

    private ChatClient chatClient;
    private OpenAiImageModel openAiImageModel;

    public ImageGenController(OpenAiImageModel openAiImageModel, ChatClient.Builder builder){
        this.openAiImageModel = openAiImageModel;
        this.chatClient = builder.build();
    }

    @GetMapping("/image/{query}")
    public String genImage(@PathVariable String query){
        ImagePrompt prompt = new ImagePrompt(query);
        ImageResponse response = openAiImageModel.call(prompt);
        return response.getResult().getOutput().getUrl();
    }

    @PostMapping("/image/describe")
    public String descImage(@RequestParam String query, @RequestParam MultipartFile file){
        return chatClient.prompt()
                .user(us -> us.text(query)
                        .media(MimeTypeUtils.IMAGE_JPEG, file.getResource()))
                .call()
                .content();
    }
}

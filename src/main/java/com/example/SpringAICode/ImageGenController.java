package com.example.SpringAICode;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.model.Media;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageGenController {

    private ChatClient chatClient;

    // 1. Constructor Injection: Application startup par ChatClient inject hoga
    public ImageGenController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @PostMapping("/image/describe")
    public String descImage(@RequestParam String query, @RequestParam MultipartFile file) {

        // 2. Content Type dynamically determine karo, fall back to JPEG
        String contentType = file.getContentType();
        var mimeType = (contentType != null) ? MimeTypeUtils.parseMimeType(contentType) : MimeTypeUtils.IMAGE_JPEG;

        // 3. Media object explicit banao
        Media imageMedia = new Media(mimeType, file.getResource());

        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(query)
                        .media(imageMedia))
                .options(OllamaOptions.builder()
                        .withModel("moondream")
                        .build())
                .call()
                .content();
    }


//    private OpenAiImageModel openAiImageModel;
//
//    public ImageGenController(OpenAiImageModel openAiImageModel, ChatClient.Builder builder){
//        this.openAiImageModel = openAiImageModel;
//        this.chatClient = builder.build();
//    }
//
//    @GetMapping("/image/{query}")
//    public String genImage(@PathVariable String query){
//        // Without Options
//        ImagePrompt prompt = new ImagePrompt(query);
//
//        // With Image Options
//        ImagePrompt prompt_Option = new ImagePrompt(query, OpenAiImageOptions.builder()
//                .quality("hd")
//                .height(1024)
//                .width(1024)
//                .style("natural")
//                .build());
//        ImageResponse response = openAiImageModel.call(prompt_Option);
//        return response.getResult().getOutput().getUrl();
//    }

//    @PostMapping("/image/describe")
//    public String descImage(@RequestParam String query, @RequestParam MultipartFile file){
//        return chatClient.prompt()
//                .user(us -> us.text(query)
//                        .media(MimeTypeUtils.IMAGE_JPEG, file.getResource()))
//                .call()
//                .content();
//    }

}

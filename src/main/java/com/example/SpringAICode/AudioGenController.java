package com.example.SpringAICode;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AudioGenController {

    private OpenAiAudioTranscriptionModel audioModel;

    public AudioGenController(OpenAiAudioTranscriptionModel audioModel){
        this.audioModel = audioModel;
    }

    @PostMapping("api/stt")
    public String speechToText(@RequestParam MultipartFile file){
        return audioModel.call(file.getResource()); // sends audio to the model and returns transcribed text.
    }
}
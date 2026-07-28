package com.example.SpringAICode;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AudioGenController {

    private OpenAiAudioTranscriptionModel audioModel;
    private OpenAiAudioSpeechModel audioSpeechModel;

    public AudioGenController(OpenAiAudioTranscriptionModel audioModel, OpenAiAudioSpeechModel audioSpeechModel){
        this.audioModel = audioModel;
        this.audioSpeechModel = audioSpeechModel;
    }

    // Audio Transcription model
//    @PostMapping("api/stt")
//    public String speechToText(@RequestParam MultipartFile file){
//        return audioModel.call(file.getResource()); // sends audio to the model and returns transcribed text.
//    }

    // Audio transcription model with options
    @PostMapping("api/stt")
    public String speechToText(@RequestParam MultipartFile file){
        OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
                .language("es")
                .responseFormate(OpenAiAudioApi.TranscriptResponseFormate.SRT)
                .build();

        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(file.getResource(), options);

        return audioModel.call(prompt)
                .getResult().getOutput();
    }



//    // Audio Speech Model
//    @PostMapping("api/tts")
//    public byte[] tts(@RequestParam String text){
//        return  audioSpeechModel.call(text);
//    }


    // Audio Speech Model Options
    @PostMapping("api/tts")
    public byte[] tts(@RequestParam String text){
        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
                .speed(1.5f)
                .voice(OpenAiAudioApi.SpeechRequest.Voice.NOVA)
                .build();

        SpeechPrompt prompt = new SpeechPrompt(text, options);

        return audioSpeechModel.call(prompt)
                .getResult().getOutput();
    }
}
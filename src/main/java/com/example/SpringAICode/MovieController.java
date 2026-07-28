package com.example.SpringAICode;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.stringtemplate.v4.ST;

import java.util.List;

public class MovieController {

    private ChatClient chatClient;

    public MovieController(OllamaChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }

    @GetMapping("movies")
    public List<String> getMovies(@RequestParam String name){
        List<String> movies = chatClient.prompt()
                .user(u -> u.text("List top 5 movies of {name}").param("name", name))
                .call()
                .entity(new ListOutputConverter(new DefaultConversionService()));

        return movies;
    }


    @GetMapping("/movie")
    public Movie getMovieDate(@RequestParam String name){
        BeanOutputConverter<Movie> opCon = new BeanOutputConverter<Movie>(Movie.class);

        Movie movie = chatClient.prompt()
                .user(u -> u.text("Get me the best movie of {name}").param("name", name))
                .call()
                .entity(new BeanOutputConverter<Movie>(Movie.class));

        return movie;
    }
}

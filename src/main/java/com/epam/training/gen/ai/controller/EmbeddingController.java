package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.RequestEmbedding;
import com.epam.training.gen.ai.service.TextEmbeddingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class EmbeddingController {
    private final TextEmbeddingService textEmbeddingService;

    public EmbeddingController(TextEmbeddingService textEmbeddingService) {
        this.textEmbeddingService = textEmbeddingService;
    }

    @PostMapping("/embedding-insert")
    public void embeddingInsert(@RequestBody RequestEmbedding contents){
        textEmbeddingService.insert(contents != null ? contents.getContents() : List.of());
    }

    @GetMapping("/embedding-search")
    public List<String> embeddingSearch(@RequestParam("input") String input){
        return textEmbeddingService.search(input);
    }
}

package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.ImagePromptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
public class ImageController {
    final ImagePromptService imagePromptService;

    public ImageController( ImagePromptService imagePromptService) {
        this.imagePromptService = imagePromptService;
    }

    @GetMapping("/image-chat")
    public String historyChat(@RequestParam("input") String input) throws InterruptedException, IOException {
        return imagePromptService.generateImage(input);
    }
}

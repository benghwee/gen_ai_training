package com.epam.training.gen.ai;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatController {
    final PromptService promptService;

    public ChatController(PromptService promptService) {
        this.promptService = promptService;
    }

    @GetMapping("/chat")
    public List<String> chat(@RequestParam("input") String input){
        return promptService.getChatCompletions(input).stream().toList();
    }
}


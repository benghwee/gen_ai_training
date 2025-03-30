package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.ChatPromptService;
import com.epam.training.gen.ai.service.TextEmbeddingService;
import com.epam.training.gen.ai.util.TemplatePopulator;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class ChatController {

    final ChatPromptService chatPromptService;
    final TextEmbeddingService textEmbeddingService;
    final HashMap<String, ChatHistory> chatHistoryCache = new HashMap<>();

    public ChatController(ChatPromptService chatPromptService, TextEmbeddingService textEmbeddingService) {
        this.chatPromptService = chatPromptService;
        this.textEmbeddingService = textEmbeddingService;
    }

    @PostMapping("/chat")
    public String historyChat(@RequestBody PromptExecutionSettings promptExecutionSettings,
                                    @RequestParam("input") String input,
                                    @RequestParam("chatId") String chatId,
                                    @RequestParam(name = "modelId", required=false) String modelId){

        // Get any history by chatId
        var chatHistory = getChatHistory(searchEmbedding(input), chatId);
        var result = chatPromptService.getChatCompletions(chatHistory, modelId, promptExecutionSettings);
        // Print question and answer logs.
        log.info("Question : {}", input);
        var filteredResult = result.stream().filter(s -> s != null && !s.isEmpty()).toList();
        String output = String.join("\n", filteredResult);
        log.info("{}", output);
        return output;
    }

    private String searchEmbedding(String input)  {
        // First search in the text embedding store
        // set threshold to a lower value. Else maybe not result when the match score is all low.
        var results = textEmbeddingService.search(input , 0.2);
        try {
            var output = TemplatePopulator.populateTemplate(input, results);
            log.info(output);
            return output;
        } catch (IOException e) {
            log.error(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    private ChatHistory getChatHistory(String input, String chatId){
        // Each chatId with its own context
        var chatHistory = this.chatHistoryCache.get(chatId);
        if(chatHistory != null) {
            log.info("Chat id {} with history found." , chatId);
            chatHistory.addUserMessage(input);
            return chatHistory;
        }else {
            log.info("Creating new chat history with chat Id {}." , chatId);
            var newChat = new ChatHistory();
            newChat.addUserMessage(input);
            this.chatHistoryCache.put(chatId, newChat);
            return newChat;
        }
    }
}

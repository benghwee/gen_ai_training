package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.ChatPromptService;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class ChatController {

    final ChatPromptService historyPromptService;
    final HashMap<String, ChatHistory> chatHistoryCache = new HashMap<>();

    public ChatController(ChatPromptService historyPromptService) {
        this.historyPromptService = historyPromptService;
    }

    @PostMapping("/chat")
    public List<String> historyChat(@RequestBody PromptExecutionSettings promptExecutionSettings,
                                    @RequestParam("input") String input,
                                    @RequestParam("chatId") String chatId,
                                    @RequestParam(name = "modelId", required=false) String modelId){
        // Get any history by chatId
        var chatHistory = getChatHistory(input, chatId);
        var result = historyPromptService.getChatCompletions(chatHistory,modelId, promptExecutionSettings);
        // Print question and answer logs.
        log.info("Question : {}", input);
        log.info("{}", String.join("\n", result));
        return result;
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

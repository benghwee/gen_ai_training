package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.HistoryPromptService;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class HistoryChatController {

    final HistoryPromptService historyPromptService;
    final HashMap<String, ChatHistory> chatHistoryCache = new HashMap<>();

    public HistoryChatController(HistoryPromptService historyPromptService) {
        this.historyPromptService = historyPromptService;
    }

    @GetMapping("/history-chat")
    public List<String> historyChat(@RequestParam("input") String input, @RequestParam("chatId") String chatId){
        // Get any history by chatId
        var chatHistory = getChatHistory(input, chatId);
        var result = historyPromptService.getChatCompletions(chatHistory);
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

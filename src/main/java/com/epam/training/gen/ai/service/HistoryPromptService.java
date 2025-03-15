package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class HistoryPromptService {
    final ChatCompletionService chatCompletionService;
    final Kernel kernel;
    final InvocationContext invocationContext;

    public HistoryPromptService(ChatCompletionService chatCompletionService, Kernel kernel, InvocationContext invocationContext) {
        this.chatCompletionService = chatCompletionService;
        this.kernel = kernel;
        this.invocationContext = invocationContext;
    }

    public List<String> getChatCompletions(ChatHistory chatHistory){
        List<ChatMessageContent<?>> results = chatCompletionService.getChatMessageContentsAsync(
                chatHistory,
                kernel,
                invocationContext
        ).block();
        assert results != null;
        chatHistory.addAll(results);
        return results.stream().map(ChatMessageContent::getContent).toList();
    }
}

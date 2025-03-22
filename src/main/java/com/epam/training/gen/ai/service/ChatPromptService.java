package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.provider.ChatCompletionProvider;
import com.epam.training.gen.ai.provider.InvocationContextProvider;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class ChatPromptService {
    final Kernel kernel;
    final ChatCompletionProvider chatCompletionProvider;
    final InvocationContextProvider invocationContextProvider;

    public ChatPromptService(ChatCompletionProvider chatCompletionProvider, Kernel kernel, InvocationContextProvider invocationContextProvider) {
        this.kernel = kernel;
        this.chatCompletionProvider = chatCompletionProvider;
        this.invocationContextProvider = invocationContextProvider;
    }

    public List<String> getChatCompletions(ChatHistory chatHistory, String deploymentName, PromptExecutionSettings settings){
        ChatCompletionService chatCompletionService = chatCompletionProvider.getChatCompletionService(deploymentName);
        InvocationContext invocationContext = invocationContextProvider.createInvocationContext(settings);
        log.info("Model Id : {}", chatCompletionService.getModelId());
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

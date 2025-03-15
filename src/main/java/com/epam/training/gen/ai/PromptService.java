package com.epam.training.gen.ai;


import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PromptService {

    private final OpenAIAsyncClient aiAsyncClient;
    private final String deploymentOrModelName;

    public PromptService(OpenAIAsyncClient aiAsyncClient,
                         @Value("${client-openai-deployment-name}") String deploymentOrModelName) {
        this.aiAsyncClient = aiAsyncClient;
        this.deploymentOrModelName = deploymentOrModelName;
    }

    public List<String> getChatCompletions(String input) {
        var completions = aiAsyncClient
                .getChatCompletions(
                        deploymentOrModelName,
                        new ChatCompletionsOptions(
                                List.of(new ChatRequestUserMessage(input))))
                .block();
        assert completions != null;
        var messages = completions.getChoices().stream()
                .map(c -> c.getMessage().getContent())
                .toList();
        log.info(messages.toString());
        return messages;
    }
}

package com.epam.training.gen.ai.provider;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatCompletionProvider {

    private final Map<String, ChatCompletionService> chatCompletionServices ;

    public ChatCompletionProvider(List<String> openAiDeploymentNames, OpenAIAsyncClient openAIAsyncClient) {
        chatCompletionServices = new HashMap<>();
        // Create multiple ChatCompletionService and cache into the map
        for(String deploymentName: openAiDeploymentNames){
            var client = OpenAIChatCompletion.builder()
                    .withModelId(deploymentName)
                    .withOpenAIAsyncClient(openAIAsyncClient)
                    .build();
            chatCompletionServices.put(deploymentName, client);
        }
    }

    public ChatCompletionService getChatCompletionService(String deploymentName){
        var completionService = chatCompletionServices.get(deploymentName);
        // if you can't find the deployment name (chat engine), return first service available
        return completionService != null ? completionService : chatCompletionServices.values().stream().findFirst().orElse(null);
    }
}

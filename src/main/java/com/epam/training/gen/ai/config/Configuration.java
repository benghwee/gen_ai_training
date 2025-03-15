package com.epam.training.gen.ai.config;


import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Value("${client-openai-key}")
    private String openAiKey;

    @Value("${client-openai-endpoint}")
    private String openAiEndpoint;

    @Value("${client-openai-deployment-name}")
    private String openAiDeploymentName;

    @Value("${prompt-config-temperature}")
    private double temperature;

    @Value("${prompt-config-presence-max_tokens}")
    private int maxTokens;

    @Value("${prompt-config-presence-penalty}")
    private double presencePenalty;

    @Value("${prompt-config-frequency-penalty}")
    private double frequencyPenalty;

    @Bean
    public OpenAIAsyncClient openAIAsyncClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(openAiKey))
                .endpoint(openAiEndpoint)
                .buildAsyncClient();
    }

    @Bean
    public ChatCompletionService chatCompletionService(OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withModelId(openAiDeploymentName)
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }

    @Bean
    public Kernel kernel(ChatCompletionService chatCompletionService) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .build();
    }

    @Bean
    public InvocationContext invocationContext() {
        return InvocationContext.builder()
                .withPromptExecutionSettings(PromptExecutionSettings.builder()
                        .withTemperature(temperature)
                        .withMaxTokens(maxTokens)
                        .withPresencePenalty(presencePenalty)
                        .withFrequencyPenalty(frequencyPenalty)
                        .build())
                .build();
    }

    @Bean
    public Map<String, PromptExecutionSettings> promptExecutionsSettingsMap() {
        return Map.of(openAiDeploymentName, PromptExecutionSettings.builder()
                .withTemperature(temperature)
                .withMaxTokens(maxTokens)
                .withPresencePenalty(presencePenalty)
                .withFrequencyPenalty(frequencyPenalty)
                .build());
    }
}

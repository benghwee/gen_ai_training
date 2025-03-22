package com.epam.training.gen.ai.config;


import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.epam.training.gen.ai.provider.ChatCompletionProvider;
import com.epam.training.gen.ai.provider.InvocationContextProvider;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionFromPrompt;
import com.microsoft.semantickernel.services.AIServiceCollection;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.List;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Value("${client-openai-key}")
    private String openAiKey;

    @Value("${client-openai-endpoint}")
    private String openAiEndpoint;

    @Value("#{'${client-openai-deployment-names}'.split(',')}")
    private List<String> openAiDeployments;

    @Value("${prompt-config-temperature}")
    private double temperature;

    @Value("${prompt-config-presence-max_tokens}")
    private int maxTokens;

    @Value("${prompt-config-presence-penalty}")
    private double presencePenalty;

    @Value("${prompt-config-frequency-penalty}")
    private double frequencyPenalty;

    @Bean
    public ChatCompletionProvider chatCompletionProvider(OpenAIAsyncClient openAIAsyncClient){
        return new ChatCompletionProvider(openAiDeployments, openAIAsyncClient);
    }

    @Bean
    public InvocationContextProvider invocationContextProvider(){
        return new InvocationContextProvider(temperature,maxTokens,presencePenalty,frequencyPenalty);
    }

    @Bean
    public OpenAIAsyncClient openAIAsyncClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(openAiKey))
                .endpoint(openAiEndpoint)
                .buildAsyncClient();
    }

    @Bean
    public Kernel kernel() {
        return Kernel.builder()
                .build();
    }
}

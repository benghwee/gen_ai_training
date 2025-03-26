package com.epam.training.gen.ai.config;


import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.epam.training.gen.ai.plugin.BmiCalculatorPlugin;
import com.epam.training.gen.ai.plugin.CurrencyRatePlugin;
import com.epam.training.gen.ai.provider.ChatCompletionProvider;
import com.epam.training.gen.ai.provider.InvocationContextProvider;
import com.epam.training.gen.ai.provider.MemoryVectorStoreProvider;
import com.github.jknack.handlebars.internal.antlr.atn.PredicateTransition;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.textembedding.OpenAITextEmbeddingGenerationService;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.List;

@org.springframework.context.annotation.Configuration
public class Configuration {

    public static final int EMBEDDING_DIMENSION = 512;//1536;

    @Value("${client-openai-key}")
    private String openAiKey;

    @Value("${client-openai-endpoint}")
    private String openAiEndpoint;

    @Value("#{'${client-openai-deployment-names}'.split(',')}")
    private List<String> openAiDeployments;

    @Value("${client-openai-embedding-deployment-name}")
    private String openAiEmbeddingDeploymentName;

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
    public OpenAITextEmbeddingGenerationService openAITextEmbeddingGenerationService(OpenAIAsyncClient client){
        return OpenAITextEmbeddingGenerationService.builder()
                .withOpenAIAsyncClient(client)
                .withModelId(openAiEmbeddingDeploymentName)
                .withDimensions(EMBEDDING_DIMENSION)
                .build();
    }

    @Bean
    public MemoryVectorStoreProvider memoryVectorStoreProvider(OpenAITextEmbeddingGenerationService openAITextEmbeddingGenerationService){
        return new MemoryVectorStoreProvider(openAITextEmbeddingGenerationService);
    }

    @Bean
    public List<KernelPlugin> kernelPlugins() {
        return List.of(
                KernelPluginFactory.createFromObject(new BmiCalculatorPlugin(), "BmiCalculatorPlugin"),
                KernelPluginFactory.createFromObject(new CurrencyRatePlugin(), "CurrencyRatePlugin"));
    }

    @Bean
    public Kernel kernel(List<KernelPlugin> plugins) {
        var builder =  Kernel.builder();
        plugins.forEach(builder::withPlugin);
        return builder.build();
        /**
        Alternative way to insert multiple plugin 
        return Kernel.builder()
                .withPlugin(KernelPluginFactory.createFromObject(new BmiCalculatorPlugin(), "BmiCalculatorPlugin"))
                .withPlugin(KernelPluginFactory.createFromObject(new CurrencyRatePlugin(), "CurrencyRatePlugin"))
                .build();
       **/
    }
}

package com.epam.training.gen.ai.provider;

import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

public class InvocationContextProvider {

    private double temperature;

    private int maxTokens;

    private double presencePenalty;

    private double frequencyPenalty;

    public InvocationContextProvider(double temperature, int maxTokens, double presencePenalty, double frequencyPenalty){
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.presencePenalty = presencePenalty;
        this.frequencyPenalty = frequencyPenalty;
    }

    public InvocationContext createInvocationContext(PromptExecutionSettings settings){
        if(settings == null){
            // return default InvocationContext with value specified in properties file.
            return InvocationContext.builder()
                    .withPromptExecutionSettings(PromptExecutionSettings.builder()
                            .withTemperature(temperature)
                            .withMaxTokens(maxTokens)
                            .withPresencePenalty(presencePenalty)
                            .withFrequencyPenalty(frequencyPenalty)
                            .build())
                    .build();
        }
        // return InvocationContext base on user input
        return InvocationContext.builder()
                .withPromptExecutionSettings(settings)
                .build();
    }
}

package com.epam.training.gen.ai;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatResponseMessage;
import com.epam.training.gen.ai.service.PromptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromptServiceTest {

    @Mock
    private OpenAIAsyncClient aiAsyncClient;

    @InjectMocks
    private PromptService promptService;

    private final String deploymentOrModelName = "test-deployment";

    @BeforeEach
    void setUp() {
        promptService = new PromptService(aiAsyncClient, deploymentOrModelName);
    }

    @Test
    void getChatCompletions_ShouldReturnMockedResponse() {
        // Given
        String input = "Hello, AI!";
        String responseMessage = "Hello, human!";

        // Mock ChatResponseMessage
        ChatResponseMessage mockResponseMessage = mock(ChatResponseMessage.class);
        when(mockResponseMessage.getContent()).thenReturn(responseMessage);

        // Mock ChatChoice
        ChatChoice chatChoice = mock(ChatChoice.class);
        when(chatChoice.getMessage()).thenReturn(mockResponseMessage);

        // Mock ChatCompletions
        ChatCompletions chatCompletions = mock(ChatCompletions.class);
        when(chatCompletions.getChoices()).thenReturn(List.of(chatChoice));

        // ChatCompletionsOptions object
        when(aiAsyncClient.getChatCompletions(eq(deploymentOrModelName), any(ChatCompletionsOptions.class)))
                .thenReturn(Mono.just(chatCompletions));

        // When
        List<String> responses = promptService.getChatCompletions(input);

        // Then
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(responseMessage, responses.get(0));

        verify(aiAsyncClient, times(1))
                .getChatCompletions(eq(deploymentOrModelName), any(ChatCompletionsOptions.class));
    }
}

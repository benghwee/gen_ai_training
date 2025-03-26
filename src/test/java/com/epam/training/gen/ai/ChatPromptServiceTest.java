package com.epam.training.gen.ai;

import com.epam.training.gen.ai.provider.ChatCompletionProvider;
import com.epam.training.gen.ai.provider.InvocationContextProvider;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import com.epam.training.gen.ai.service.ChatPromptService;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatPromptServiceTest {

    @Mock
    private ChatCompletionService chatCompletionService;

    @Mock
    private Kernel kernel;

    @InjectMocks
    private ChatPromptService historyPromptService;

    @Mock
    private ChatCompletionProvider chatCompletionProvider;

    @Mock
    private InvocationContextProvider invocationContextProvider;


    @BeforeEach
    void setUp() {

    }

    @Test
    void getChatCompletions_ShouldReturnMockedResponse() {
        // Given
        String responseMessage1 = "responseMessage1";
        String responseMessage2 = "responseMessage2";
        String deploymentName = "gpt-35-turbo";
        PromptExecutionSettings settings = PromptExecutionSettings.builder()
                .withTemperature(1.0)
                .build();

        ChatHistory chatHistory = mock(ChatHistory.class);
        InvocationContext invocationContext = mock(InvocationContext.class);
        ChatMessageContent<String> content1 = mock(ChatMessageContent.class);
        ChatMessageContent<String> content2 = mock(ChatMessageContent.class);

        when(content1.getContent()).thenReturn(responseMessage1);
        when(content2.getContent()).thenReturn(responseMessage2);
        List<ChatMessageContent<?>> mockResponses = List.of(content1,content2);
        when(chatCompletionService.getChatMessageContentsAsync(chatHistory, kernel, invocationContext))
                .thenReturn(Mono.just(mockResponses));
        when(chatCompletionProvider.getChatCompletionService(deploymentName)).thenReturn(chatCompletionService);
        when(invocationContextProvider.createInvocationContext(settings)).thenReturn(invocationContext);

        // When
        List<String> responses = historyPromptService.getChatCompletions(chatHistory,deploymentName, settings);

        // Then
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(2, responses.size());
        assertEquals(responseMessage1, responses.get(0));
        assertEquals(responseMessage2, responses.get(1));

        verify(chatCompletionService, times(1)).getChatMessageContentsAsync(chatHistory, kernel, invocationContext);
    }

}


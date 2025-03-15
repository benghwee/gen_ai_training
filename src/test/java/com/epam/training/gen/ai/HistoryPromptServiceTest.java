package com.epam.training.gen.ai;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
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
import com.epam.training.gen.ai.service.HistoryPromptService;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryPromptServiceTest {

    @Mock
    private ChatCompletionService chatCompletionService;

    @Mock
    private Kernel kernel;

    @InjectMocks
    private HistoryPromptService historyPromptService;


    @BeforeEach
    void setUp() {

    }

    @Test
    void getChatCompletions_ShouldReturnMockedResponse() {
        // Given
        String responseMessage1 = "responseMessage1";
        String responseMessage2 = "responseMessage2";

        ChatHistory chatHistory = mock(ChatHistory.class);
        InvocationContext invocationContext = mock(InvocationContext.class);
        ChatMessageContent<String> content1 = mock(ChatMessageContent.class);
        ChatMessageContent<String> content2 = mock(ChatMessageContent.class);

        when(content1.getContent()).thenReturn(responseMessage1);
        when(content2.getContent()).thenReturn(responseMessage2);
        List<ChatMessageContent<?>> mockResponses = List.of(content1,content2);
        when(chatCompletionService.getChatMessageContentsAsync(chatHistory, kernel, null))
                .thenReturn(Mono.just(mockResponses));

        // When
        List<String> responses = historyPromptService.getChatCompletions(chatHistory);

        // Then
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(2, responses.size());
        assertEquals(responseMessage1, responses.get(0));
        assertEquals(responseMessage2, responses.get(1));

        verify(chatCompletionService, times(1)).getChatMessageContentsAsync(chatHistory, kernel, null);
    }

}


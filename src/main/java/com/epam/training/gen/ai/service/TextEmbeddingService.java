package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.EmbeddingFile;
import com.epam.training.gen.ai.provider.MemoryVectorStoreProvider;
import com.microsoft.semantickernel.data.vectorsearch.VectorSearchResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class TextEmbeddingService {
    private final MemoryVectorStoreProvider memoryVectorStoreProvider;

    public TextEmbeddingService(MemoryVectorStoreProvider memoryVectorStoreProvider) {
        this.memoryVectorStoreProvider = memoryVectorStoreProvider;
    }

    public void insert(List<String> embeddingTextList){
        memoryVectorStoreProvider.insert(embeddingTextList);
    }

    public List<String> search(String searchText){
        VectorSearchResults<EmbeddingFile> results = memoryVectorStoreProvider.search(searchText);
        log.info("Search result :");
        return results.getResults().stream().peek(result -> {})
                .map(result -> {
                    String output = String.format("Score: %s Content: %s", result.getScore(), result.getRecord().getContent());
                    log.info(output);
                    return output;
                }).toList();
    }
}

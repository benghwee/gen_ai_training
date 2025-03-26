package com.epam.training.gen.ai.provider;

import com.epam.training.gen.ai.model.EmbeddingFile;
import com.microsoft.semantickernel.aiservices.openai.textembedding.OpenAITextEmbeddingGenerationService;
import com.microsoft.semantickernel.data.VolatileVectorStore;
import com.microsoft.semantickernel.data.VolatileVectorStoreRecordCollectionOptions;
import com.microsoft.semantickernel.data.vectorsearch.VectorSearchResults;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
public class MemoryVectorStoreProvider {

    private static final String VECTOR_STORE_NAME = "embedding_lab";

    private final OpenAITextEmbeddingGenerationService openAITextEmbeddingService;

    private final VolatileVectorStore volatileVectorStore = new VolatileVectorStore();

    private final VolatileVectorStoreRecordCollectionOptions<EmbeddingFile> options =
            VolatileVectorStoreRecordCollectionOptions.<EmbeddingFile>builder()
                    .withRecordClass(EmbeddingFile.class)
                    .build();

    public MemoryVectorStoreProvider(OpenAITextEmbeddingGenerationService openAITextEmbeddingService) {
        this.openAITextEmbeddingService = openAITextEmbeddingService;
        var collection = volatileVectorStore.getCollection(VECTOR_STORE_NAME, options);
        // Create collection if it does not exist and store data
        collection.createCollectionIfNotExistsAsync().block();
    }

    public void insert(List<String> embeddingTextList) {
        var collection = volatileVectorStore.getCollection(VECTOR_STORE_NAME, options);
        // Generate embedding for the list of text
        embeddingTextList.forEach(text -> {
            var embeddings = openAITextEmbeddingService.generateEmbeddingsAsync(Collections.singletonList(text));
            embeddings.flatMap(item -> {
                                EmbeddingFile file = new EmbeddingFile(
                                        UUID.randomUUID().toString(),
                                        text,
                                        item.get(0).getVector()
                                );
                                log.info("Insert text {}", text);
                                return collection.upsertAsync(file, null);
                            }
                    ).block();
        });
    }

    public VectorSearchResults<EmbeddingFile> search(String searchText){
        var collection = volatileVectorStore.getCollection(VECTOR_STORE_NAME, options);
        return openAITextEmbeddingService.generateEmbeddingAsync(searchText)
                .flatMap(r -> collection.searchAsync(r.getVector(), null)).block();

    }
}

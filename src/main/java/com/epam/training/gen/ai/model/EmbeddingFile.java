package com.epam.training.gen.ai.model;

import com.microsoft.semantickernel.data.vectorstorage.annotations.VectorStoreRecordData;
import com.microsoft.semantickernel.data.vectorstorage.annotations.VectorStoreRecordKey;
import com.microsoft.semantickernel.data.vectorstorage.annotations.VectorStoreRecordVector;
import com.microsoft.semantickernel.data.vectorstorage.definition.DistanceFunction;
import com.microsoft.semantickernel.data.vectorstorage.definition.IndexKind;

import java.util.List;

import static com.epam.training.gen.ai.config.Configuration.EMBEDDING_DIMENSION;

public class EmbeddingFile {
    @VectorStoreRecordKey
    private final String id;
    @VectorStoreRecordData
    private final String content;
    @VectorStoreRecordVector(dimensions = EMBEDDING_DIMENSION, indexKind = IndexKind.HNSW, distanceFunction = DistanceFunction.COSINE_SIMILARITY)
    private final List<Float> embedding;

    public EmbeddingFile(String id, String content, List<Float> embedding) {
        this.id = id;
        this.content = content;
        this.embedding = embedding;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public List<Float> getEmbedding() {
        return embedding;
    }
}

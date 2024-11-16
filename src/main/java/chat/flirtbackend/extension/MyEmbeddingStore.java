package chat.flirtbackend.extension;

import chat.flirtbackend.dto.MemoryDTO;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;

import java.util.List;

public interface MyEmbeddingStore {
    String add(String message, Long userId, Long characterId);
    List<EmbeddingMatch<TextSegment>> findRelevant(String text, int maxResult, double minScore, Long userId, Long characterId);
    List<EmbeddingMatch<TextSegment>> findRelevant(Embedding referenceEmbedding, int maxResult, double minScore, Long userId, Long characterId);
    List<MemoryDTO> findMemory(Embedding referenceEmbedding, int maxResult, double minScore, String conditions);
    List<MemoryDTO> findMemory(String userMessage, int maxResult, double minScore, Long userId, Long characterId);
    void delete(EmbeddingMatch<TextSegment> embedding, Long userId);
}

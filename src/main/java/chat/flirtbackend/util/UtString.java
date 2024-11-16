package chat.flirtbackend.util;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UtString {
    public static List<UUID> getUUIDFromStr(List<String> ids) {
        return ids.stream()
                .map(UUID::fromString)
                .collect(Collectors.toList());
    }

    public static List<UUID> getUUIDFromEmbedding(List<EmbeddingMatch<TextSegment>> embeddings) {
        return embeddings.stream()
                .map(id -> UUID.fromString(id.embeddingId()))
                .collect(Collectors.toList());
    }

    public static UUID getUUIDFromEmbedding(EmbeddingMatch<TextSegment> embedding) {
        return UUID.fromString(embedding.embeddingId());
    }
}

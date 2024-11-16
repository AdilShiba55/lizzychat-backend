package chat.flirtbackend.dto;

import dev.langchain4j.data.embedding.Embedding;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoryDTO {
    private String embeddingId;
    private String text;
    private Double score;
    private Long userId;

    public MemoryDTO(String embeddingId, String text, Double score, Long userId) {
        this.embeddingId = embeddingId;
        this.text = text;
        this.score = score;
        this.userId = userId;
    }
}

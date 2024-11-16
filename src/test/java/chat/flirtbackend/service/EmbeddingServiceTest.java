package chat.flirtbackend.service;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmbeddingServiceTest {

    private final EmbeddingService embeddingService;

    @Autowired
    public EmbeddingServiceTest(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @Test
    void add() {
        embeddingService.add("I like ice cream", 1L, 1L);
    }
}
package chat.flirtbackend.service;

import chat.flirtbackend.dto.MemoryDTO;
import chat.flirtbackend.entity.Memory;
import chat.flirtbackend.extension.MyEmbeddingStore;
import chat.flirtbackend.mapper.MemoryMapper;
import chat.flirtbackend.repository.MemoryRepository;
import chat.flirtbackend.util.UtString;
import com.pgvector.PGvector;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static dev.langchain4j.internal.Utils.randomUUID;

@Service
public class EmbeddingService implements MyEmbeddingStore {
    private final MemoryRepository memoryRepository;
    private final EmbeddingModel embeddingModel;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @SneakyThrows
    public EmbeddingService(MemoryRepository memoryRepository, EmbeddingModel embeddingModel, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.memoryRepository = memoryRepository;
        this.embeddingModel = embeddingModel;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        PGvector.addVectorType(namedParameterJdbcTemplate.getJdbcTemplate().getDataSource().getConnection());
    }

    @Override
    public String add(String message, Long userId, Long characterId) {
        Embedding embedding = embeddingModel.embed(message).content();
        String id = randomUUID();
        Memory memory = new Memory();
        memory.setEmbeddingId(UUID.fromString(id));
        memory.setEmbedding(embedding.vectorAsList());
        memory.setText(message);
        memory.setUserId(userId);
        memory.setCharacterId(characterId);
        memoryRepository.save(memory);
        return id;
    }

    public List<EmbeddingMatch<TextSegment>> findRelevant(String text, int maxResult, double minScore, Long userId, Long characterId) {
        Embedding referenceEmbedding = embeddingModel.embed(text).content();
        return findRelevant(referenceEmbedding, maxResult, minScore, userId, characterId);
    }

    public List<EmbeddingMatch<TextSegment>> findRelevant(Embedding referenceEmbedding, int maxResult, double minScore, Long userId, Long characterId) {
        String condition = "user_id = " + userId + " and character_id  = " + characterId + "\n";
        return findMemory(referenceEmbedding, maxResult, minScore, condition).stream()
                .map(memory -> new EmbeddingMatch<>(memory.getScore(), memory.getEmbeddingId(), null, TextSegment.from(memory.getText())))
                .collect(Collectors.toList());
    }

    public List<MemoryDTO> findMemory(Embedding referenceEmbedding, int maxResult, double minScore, String conditions) {
        String referenceVector = Arrays.toString(referenceEmbedding.vector());
        String query = String.format("" +
                "with temp as (select (2 - (embedding <=> '%s')) / 2 AS score, embedding_id, embedding, text, user_id, character_id from memory)\n" +
                "select * from temp where %s and score >= %s order by score desc limit %s", referenceVector, conditions, minScore, maxResult);
        return namedParameterJdbcTemplate.query(query, new MapSqlParameterSource(), new MemoryMapper());
    }

    public List<MemoryDTO> findMemory(String userMessage, int maxResult, double minScore, Long userId, Long characterId) {
        Embedding referenceEmbedding = embeddingModel.embed(userMessage).content();
        String condition = "user_id = " + userId + " and character_id  = " + characterId + "\n";
        return findMemory(referenceEmbedding, maxResult, minScore, condition);
    }

    @Override
    @Transactional
    public void delete(EmbeddingMatch<TextSegment> embedding, Long userId) {
        UUID uuid = UtString.getUUIDFromEmbedding(embedding);
        memoryRepository.deleteByIdAndUserId(Collections.singletonList(uuid), userId);
    }
}

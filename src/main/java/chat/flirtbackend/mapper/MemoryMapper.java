package chat.flirtbackend.mapper;
import chat.flirtbackend.dto.MemoryDTO;
import com.pgvector.PGvector;
import dev.langchain4j.data.embedding.Embedding;
import lombok.SneakyThrows;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemoryMapper implements RowMapper<MemoryDTO> {
    @Override
    @SneakyThrows
    public MemoryDTO mapRow(ResultSet resultSet, int rowNum) {
        String embeddingId = resultSet.getString("embedding_id");
        String text = resultSet.getString("text");
        Double score = resultSet.getDouble("score");
        Long userId = resultSet.getLong("user_id");
        return new MemoryDTO(embeddingId, text, score, userId);
    }
}

package chat.flirtbackend.config;

import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingModelConfig {
    @Bean
    public EmbeddingModel localEmbeddingModel() {
        return new AllMiniLmL6V2QuantizedEmbeddingModel();
    }
}

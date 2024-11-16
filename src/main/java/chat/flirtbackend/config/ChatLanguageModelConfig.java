package chat.flirtbackend.config;

import chat.flirtbackend.dto.AiProps;
import chat.flirtbackend.dto.ChatAiProps;
import chat.flirtbackend.dto.InstructAiProps;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.mistralai.MistralAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatLanguageModelConfig {

    private final ChatAiProps chatAiProps;
    private final InstructAiProps instructAiProps;

    public ChatLanguageModelConfig(ChatAiProps chatAiProps, InstructAiProps instructAiProps) {
        this.chatAiProps = chatAiProps;
        this.instructAiProps = instructAiProps;
    }

    @Bean
    public ChatLanguageModel chatModel() {
        return MistralAiChatModel.builder()
                .baseUrl(chatAiProps.getUrl())
                .modelName(chatAiProps.getModelName())
                .apiKey(chatAiProps.getApiKey())
                .temperature(chatAiProps.getModelTemperature())
                .maxTokens(70)
                .build();
    }

    @Bean
    public ChatLanguageModel instructModel() {
        return MistralAiChatModel.builder()
                .baseUrl(instructAiProps.getUrl())
                .modelName(instructAiProps.getModelName())
                .apiKey(instructAiProps.getApiKey())
                .temperature(instructAiProps.getModelTemperature())
                .maxTokens(60)
                .build();
    }

}

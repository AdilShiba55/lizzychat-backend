package chat.flirtbackend.postProcessor;

import chat.flirtbackend.service.EmbeddingService;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
public class UserInfoMessagePostProcessor implements MessagePostProcessor {

    private final ChatLanguageModel instructModel;
    private final EmbeddingService embeddingService;

    public UserInfoMessagePostProcessor(ChatLanguageModel instructModel, EmbeddingService embeddingService) {
        this.instructModel = instructModel;
        this.embeddingService = embeddingService;
    }

    @Override
    public void process(String userMessage, Long userId, Long characterId) {
        String prompt = "" +
                "Share only useful facts about user from their message in a few words in list format. no line number, no repeat, no comment.\n" +
                "If there is no personal facts about user, just put 'null'.\n" +
                "This is user message: '" + userMessage + "'.";

        SystemMessage userInfoSystemMessage = SystemMessage.from(prompt);
        Response<AiMessage> userInfoResponse = instructModel.generate(userInfoSystemMessage);
        for(String info : userInfoResponse.content().text().split("\n")) {
            if(info.length() > 5 && (!info.isBlank() && !info.toLowerCase().contains("null"))) {
                List<EmbeddingMatch<TextSegment>> embeddings = embeddingService.findRelevant(info, 10, 0.7, userId, characterId);
                if(!embeddings.isEmpty()) {
                    embeddings.forEach(embedding -> embeddingService.delete(embedding, userId));
                }
                embeddingService.add(info, userId, characterId);
            }
        }
    }
}

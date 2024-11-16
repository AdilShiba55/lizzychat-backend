package chat.flirtbackend.postProcessor;

public interface MessagePostProcessor {
    void process(String userMessage, Long userId, Long characterId);
}

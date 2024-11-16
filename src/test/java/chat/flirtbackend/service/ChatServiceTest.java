package chat.flirtbackend.service;

import chat.flirtbackend.dto.ChatMessageResponseDTO;
import chat.flirtbackend.entity.Message;
import dev.langchain4j.data.message.SystemMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatServiceTest {

    private final ChatService chatService;

    @Autowired
    public ChatServiceTest(ChatService chatService) {
        this.chatService = chatService;
    }
}
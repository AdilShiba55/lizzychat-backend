package chat.flirtbackend.messageGenerator;

import chat.flirtbackend.service.TextMessageService;
import dev.langchain4j.data.message.SystemMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TextMessageServiceTest {

    private final TextMessageService textMessageService;

    @Autowired
    public TextMessageServiceTest(TextMessageService textMessageService) {
        this.textMessageService = textMessageService;
    }

    @Test
    void getSystemMessage() {
        String userMessage = "Test of getSystemMessage. I like eating ice cream.";
        SystemMessage systemMessage = textMessageService.getSystemMessage(userMessage, 1L, 1L);
        Assertions.assertNotNull(systemMessage);
    }

    @Test
    void getLastMessagesStr() {
        String result = textMessageService.getLastMessagesStr(1L, 1000L, 10);
        Assertions.assertNotNull(result);
    }
}
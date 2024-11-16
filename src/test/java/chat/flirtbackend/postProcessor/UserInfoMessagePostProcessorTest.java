package chat.flirtbackend.postProcessor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserInfoMessagePostProcessorTest {

    private final UserInfoMessagePostProcessor userInfoMessagePostProcessor;

    @Autowired
    public UserInfoMessagePostProcessorTest(UserInfoMessagePostProcessor userInfoMessagePostProcessor) {
        this.userInfoMessagePostProcessor = userInfoMessagePostProcessor;
    }

    @Test
    void process() {
        String text1 = "I enjoy sleeping and watching series. Tell me something interesting about your childhood.";
        String text2 = "My name is Adil and i'm 23 years old, you know?";
        userInfoMessagePostProcessor.process(text2, 1L, 1L);
    }
}
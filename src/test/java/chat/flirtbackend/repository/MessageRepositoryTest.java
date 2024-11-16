package chat.flirtbackend.repository;

import chat.flirtbackend.util.UtMessageType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageRepositoryTest {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageRepositoryTest(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
}
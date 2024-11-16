package chat.flirtbackend.repository;

import chat.flirtbackend.entity.UserVerification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserVerificationRepositoryTest {

    private final UserVerificationRepository userVerificationRepository;

    @Autowired
    public UserVerificationRepositoryTest(UserVerificationRepository userVerificationRepository) {
        this.userVerificationRepository = userVerificationRepository;
    }

    @Test
    void findLast() {
        Optional<UserVerification> item = userVerificationRepository.findLast(1L);
        Assertions.assertNotNull(item);
    }
}
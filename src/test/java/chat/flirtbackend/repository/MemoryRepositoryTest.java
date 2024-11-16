package chat.flirtbackend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemoryRepositoryTest {

    private final MemoryRepository memoryRepository;

    @Autowired
    public MemoryRepositoryTest(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }

    @Test
    @Transactional
    void deleteByIdAndUserId() {
        UUID uuid = UUID.fromString("e17d128f-ef6c-42bd-8971-126eb75167e4");
        memoryRepository.deleteByIdAndUserId(List.of(uuid), 1L);
    }
}
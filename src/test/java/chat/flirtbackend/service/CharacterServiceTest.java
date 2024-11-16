package chat.flirtbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CharacterServiceTest {

    private final CharacterService characterService;

    @Autowired
    public CharacterServiceTest(CharacterService characterService) {
        this.characterService = characterService;
    }

    @Test
    void getUnlockedCharacterIds() {
        List<Long> unlockedCharacterIds = characterService.getUnlockedCharacterIds(1L);
        Assertions.assertNotNull(unlockedCharacterIds);
    }
}
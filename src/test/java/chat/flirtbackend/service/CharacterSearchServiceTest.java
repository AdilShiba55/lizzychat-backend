package chat.flirtbackend.service;

import chat.flirtbackend.dto.CharacterChatItemDTO;
import chat.flirtbackend.mapper.CharacterChatItemMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class CharacterSearchServiceTest {

    private final CharacterSearchService characterSearchService;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public CharacterSearchServiceTest(CharacterSearchService characterSearchService, NamedParameterJdbcTemplate namedParameterJdbcTemplate, ObjectMapper objectMapper) {
        this.characterSearchService = characterSearchService;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.objectMapper = objectMapper;
    }
}
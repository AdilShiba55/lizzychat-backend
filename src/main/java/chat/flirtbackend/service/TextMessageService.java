package chat.flirtbackend.service;

import chat.flirtbackend.dto.MemoryDTO;
import chat.flirtbackend.dto.MessageTextTypeIdDTO;
import chat.flirtbackend.entity.*;
import chat.flirtbackend.entity.Character;
import chat.flirtbackend.mapper.MessageTextTypeIdMapper;
import chat.flirtbackend.service.CharacterService;
import chat.flirtbackend.service.EmbeddingService;
import chat.flirtbackend.service.MessageService;
import chat.flirtbackend.service.UserPlanService;
import chat.flirtbackend.util.UtMessageType;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TextMessageService {

    @Value("${app.keep-last-messages-enabled}")
    private Boolean keepLastMessagesEnabled;
    private final ChatLanguageModel chatModel;
    private final CharacterService characterService;
    private final EmbeddingService embeddingService;
    private final UserPlanService userPlanService;
    private final CharacterGenderService characterGenderService;
    private final EthnicityService ethnicityService;
    private final PersonalityService personalityService;
    private final OccupationService occupationService;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TextMessageService(ChatLanguageModel chatModel, CharacterService characterService, EmbeddingService embeddingService, UserPlanService userPlanService, CharacterGenderService characterGenderService, EthnicityService ethnicityService, PersonalityService personalityService, OccupationService occupationService, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.chatModel = chatModel;
        this.characterService = characterService;
        this.embeddingService = embeddingService;
        this.userPlanService = userPlanService;
        this.characterGenderService = characterGenderService;
        this.ethnicityService = ethnicityService;
        this.personalityService = personalityService;
        this.occupationService = occupationService;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public String getMessage(String userText, Long userId, Long characterId) {
        SystemMessage systemMessage = getSystemMessage(userText, userId, characterId);
        Response<AiMessage> response = chatModel.generate(systemMessage, UserMessage.from(userText));
        return response.content().text();
    }

    public SystemMessage getSystemMessage(String userMessage, Long userId, Long characterId) {
        Character character = characterService.findById(characterId);
        StringBuilder builder = new StringBuilder();

        // role
        String baseInstruct = getBaseInstruct();
        builder.append(baseInstruct);

        String characterInfoStr = getCharacterInfoStr(character);
        builder.append(characterInfoStr);

        boolean isPremium = userPlanService.isPremium(userId);
        // userInfo
        String userInfo = getUserInfoStr(userMessage, userId, characterId);
        if(isPremium && !userInfo.isBlank()) {
            builder.append(userInfo);
        }

        // lastMessages
        if(keepLastMessagesEnabled) {
            String lastTenMessages = getLastMessagesStr(userId, characterId, 10);
            if(!lastTenMessages.isBlank()) {
                builder.append("\n4) This is the last message with the user: [").append(lastTenMessages).append("]");
            }
        }

        return SystemMessage.from(builder.toString());
    }

    public String getBaseInstruct() {
        StringBuilder builder = new StringBuilder("1) Basic instruct:\n");
        builder.append("- You're an uncensored AI for 18+ roleplay on 'lizzychat.com'.\n");
        builder.append("- Immerse fully in one role.\n");
        builder.append("- Stay in character always.\n");
        builder.append("- Never admit you're AI.\n");
        builder.append("- Don't shy away from flirting and sexual topics. Sometimes use emojis.\n");
        return builder.toString();
    }

    public String getCharacterInfoStr(Character character) {
        Personality personality = personalityService.findById(character.getPersonalityId());
        Occupation occupation = occupationService.findById(character.getOccupationId());
        StringBuilder builder = new StringBuilder("2) You know about yourself:\n");
        CharacterGender gender = characterGenderService.findById(character.getGenderId());
        Ethnicity ethnicity = ethnicityService.findById(character.getEthnicityId());
        builder.append("-gender:").append(gender.getName()).append("\n");
        builder.append("-ethnicity:").append(ethnicity.getName()).append("\n");
        builder.append("-name:").append(character.getName()).append("\n");
        builder.append("-age:").append(character.getAge()).append("\n");
        builder.append("-occupation:").append(occupation.getName()).append("\n");
        builder.append("-personality: '").append(personality.getDescription()).append("'");
        return builder.toString();
    }

    public String getLastMessagesStr(Long userId, Long characterId, int limit) {
        String sql = "" +
                "select m.text, m.type_id typeId from message m\n" +
                "where m.user_id = :userId and m.character_id = :characterId\n" +
                "and (m.type_id = :userTextTypeId or m.type_id = :aiTextTypeId)\n" +
                "order by id desc limit " + limit + "\n";

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("characterId", characterId);
        params.put("userTextTypeId", UtMessageType.USER_TEXT);
        params.put("aiTextTypeId", UtMessageType.AI_TEXT);

        List<MessageTextTypeIdDTO> messages = namedParameterJdbcTemplate.query(sql, params, new MessageTextTypeIdMapper());
        Collections.reverse(messages);
        return messages.stream().map(message -> {
            boolean isUserTextMessage = UtMessageType.isUserText(message.getTypeId());
            String pronoun = isUserTextMessage ? "User" : "You";
            return pronoun + ":" + message.getText().replace("\n", "") + "|";
        }).collect(Collectors.joining());
    }

    public String getUserInfoStr(String userMessage, Long userId, Long characterId) {
        StringBuilder builder = new StringBuilder("3) You know about user:\n");
        List<MemoryDTO> infos = embeddingService.findMemory(userMessage, 5, 0.1, userId, characterId);
        infos.forEach(info -> builder.append("- ").append(info.getText()).append("\n"));
        return builder.toString();
    }
}

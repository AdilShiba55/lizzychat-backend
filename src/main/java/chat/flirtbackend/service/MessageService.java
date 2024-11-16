package chat.flirtbackend.service;

import chat.flirtbackend.dto.*;
import chat.flirtbackend.entity.Character;
import chat.flirtbackend.entity.Message;
import chat.flirtbackend.repository.MessageRepository;
import chat.flirtbackend.util.UtMessageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserPlanService userPlanService;

    public MessageService(MessageRepository messageRepository, UserPlanService userPlanService) {
        this.messageRepository = messageRepository;
        this.userPlanService = userPlanService;
    }

    public Page<Message> getMessages(Long userId, Long characterId, Pageable pageable) {
        return messageRepository.getMessages(userId, characterId, pageable);
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public Message createUserTextMessage(String text, Long userId, Long characterId) {
        Message message = Message.builder()
                .text(text)
                .userId(userId)
                .characterId(characterId)
                .typeId(UtMessageType.USER_TEXT)
                .dtCreate(new Date())
                .build();
        return save(message);
    }

    public Message createUserTextAskMessage(String text, Long userId, Long characterId) {
        Message message = Message.builder()
                .text(text)
                .userId(userId)
                .characterId(characterId)
                .typeId(UtMessageType.USER_TEXT_ASK)
                .dtCreate(new Date())
                .build();
        return save(message);
    }

    public Message createCharacterTextMessage(String text, Long userId, Long characterId) {
        Message message = Message.builder()
                .text(text)
                .userId(userId)
                .characterId(characterId)
                .typeId(UtMessageType.AI_TEXT)
                .dtCreate(new Date())
                .build();
        return save(message);
    }

    public Message createCharacterImageMessage(Long userId, Long characterId, Long characterImageId) {
        Message message = Message.builder()
                .userId(userId)
                .characterId(characterId)
                .typeId(UtMessageType.AI_IMAGE)
                .characterImageId(characterImageId)
                .dtCreate(new Date())
                .build();
        return save(message);
    }

    public long getUserLastDaysTextMessageCount(Long userId) {
        UserPlanInfoDTO plan = userPlanService.getCurrentPlan(userId);
        return messageRepository.messageCountAfter(userId, plan.getDtStart(), plan.getDtEnd(), List.of(UtMessageType.USER_TEXT));
    }
}

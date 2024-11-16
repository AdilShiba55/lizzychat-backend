package chat.flirtbackend.controller;

import chat.flirtbackend.dto.*;
import chat.flirtbackend.exception.MessageLimitReachedException;
import chat.flirtbackend.service.*;
import chat.flirtbackend.util.UtError;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    @Value("${app.message-limit-enabled}")
    private Boolean messageLimitEnabled;
    private final SystemService systemService;
    private final ChatService chatService;
    private final UserPlanService userPlanService;
    private final MessageService messageService;
    private final ImageGenerationService imageGenerationService;

    public ChatController(SystemService systemService, ChatService chatService, UserPlanService userPlanService, MessageService messageService, ImageGenerationService imageGenerationService) {
        this.systemService = systemService;
        this.chatService = chatService;
        this.userPlanService = userPlanService;
        this.messageService = messageService;
        this.imageGenerationService = imageGenerationService;
    }

    @GetMapping("/with-character")
    public ResponseEntity<GetMessagesResponseDTO> getMessagesWithCharacter(@ModelAttribute @Valid GetMessagesRequestDTO getMessagesRequest) {
        Long userId = systemService.getTokenInfo().getId();
        GetMessagesResponseDTO response = chatService.getMessagesWithCharacter(
                userId, getMessagesRequest.getCharacterId(),
                getMessagesRequest.getPageNum(), getMessagesRequest.getPageSize());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<SearchResponse<MessageChatItem>> getMessages(@ModelAttribute @Valid GetMessagesRequestDTO getMessagesRequest) {
        Long userId = systemService.getTokenInfo().getId();
        SearchResponse<MessageChatItem> response = chatService.getMessages(
                userId, getMessagesRequest.getCharacterId(),
                getMessagesRequest.getPageNum(), getMessagesRequest.getPageSize());
        return ResponseEntity.ok(response);
    }

    @SneakyThrows
    @PostMapping("/text")
    public synchronized ResponseEntity<ChatMessageResponseDTO<MessageChatItem>> sendTextMessage(@RequestBody @Valid SendTextMessageDTO sendMessage) {
        Long userId = systemService.getTokenInfo().getId();
        UserPlanInfoDTO currentPlan = userPlanService.getCurrentPlan(userId);
        long lastMonthTextMessageCount = messageService.getUserLastDaysTextMessageCount(userId);
        boolean isLimitReached = messageLimitEnabled && (lastMonthTextMessageCount >= currentPlan.getTextMessageLimit());
        UtError.throwIf(isLimitReached, new MessageLimitReachedException("text"));
        ChatMessageResponseDTO<MessageChatItem> response = chatService.sendTextMessage(sendMessage.getMessage(), userId, sendMessage.getCharacterId());
        return ResponseEntity.ok(response);
    }

    @SneakyThrows
    @PostMapping("/image")
    public synchronized ResponseEntity<ChatMessageResponseDTO<MessageChatItem>> sendImageMessage(@RequestBody @Valid SendImageMessageDTO sendMessage) {
        long startTime = System.currentTimeMillis();

        Long userId = systemService.getTokenInfo().getId();
        UserPlanInfoDTO currentPlan = userPlanService.getCurrentPlan(userId);
        long lastMonthImageMessageCount = imageGenerationService.getUserLastDaysImageMessageCount(userId);
        boolean isLimitReached = messageLimitEnabled && (lastMonthImageMessageCount >= currentPlan.getImageMessageLimit());
        UtError.throwIf(isLimitReached, new MessageLimitReachedException("image"));
        ChatMessageResponseDTO<MessageChatItem> response = chatService.sendImageMessage(sendMessage.getMessage(), userId, sendMessage.getCharacterId(), sendMessage.getCharacterImageTypeId());

        long endTime = System.currentTimeMillis();
        long durationInSeconds = (endTime - startTime) / 1000;
        if(durationInSeconds < 4) {
            Thread.sleep((3 - durationInSeconds) * 1000);
        }
        return ResponseEntity.ok(response);
    }
}

package chat.flirtbackend.service;

import chat.flirtbackend.dto.*;
import chat.flirtbackend.entity.*;
import chat.flirtbackend.entity.Character;
import chat.flirtbackend.exception.CharacterNotUnlockedException;
import chat.flirtbackend.postProcessor.MessagePostProcessor;
import chat.flirtbackend.util.*;
import kotlin.Pair;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {
    @Value("${app.post-processor-enabled}")
    private Boolean postProcessEnabled;
    private final List<MessagePostProcessor> messagePostProcessor;
    private final UserPlanService userPlanService;
    private final CharacterService characterService;
    private final CharacterImageService characterImageService;
    private final CharacterImagePromptService characterImagePromptService;
    private final MessageService messageService;
    private final TextMessageService textMessageService;
    private final ImageGenerationService imageGenerationService;
    private final MinioService minioService;

    public ChatService(List<MessagePostProcessor> messagePostProcessor, UserPlanService userPlanService, CharacterService characterService, CharacterImageService characterImageService, CharacterImagePromptService characterImagePromptService, MessageService messageService, TextMessageService textMessageService, ImageGenerationService imageGenerationService, MinioService minioService) {
        this.messagePostProcessor = messagePostProcessor;
        this.userPlanService = userPlanService;
        this.characterService = characterService;
        this.characterImageService = characterImageService;
        this.characterImagePromptService = characterImagePromptService;
        this.messageService = messageService;
        this.textMessageService = textMessageService;
        this.imageGenerationService = imageGenerationService;
        this.minioService = minioService;
    }

    @SneakyThrows
    @Transactional
    public ChatMessageResponseDTO<MessageChatItem> sendTextMessage(String text, Long userId, Long characterId) {
        throwIfCharacterNotEnabled(userId, characterId);
        // обрабатываем сообщение
        String aiText = textMessageService.getMessage(text, userId, characterId);
        messageService.createUserTextMessage(text, userId, characterId);
        Message aiTextMessage = messageService.createCharacterTextMessage(aiText, userId, characterId);
        boolean isPremium = userPlanService.isPremium(userId);
        if(postProcessEnabled && isPremium) {
            messagePostProcessor.forEach(messagePostProcessor -> messagePostProcessor.process(text, userId, characterId));
        }
        MessageChatItem messageChatItem = MessageChatItem.builder()
                .id(aiTextMessage.getId())
                .typeId(aiTextMessage.getTypeId())
                .text(aiText)
                .dtCreate(aiTextMessage.getDtCreate())
                .build();
        return new ChatMessageResponseDTO<>(messageChatItem);
    }

    @SneakyThrows
    @Transactional
    public ChatMessageResponseDTO<MessageChatItem> sendImageMessage(String text, Long userId, Long characterId, Long characterImageTypeId) {
        throwIfCharacterNotEnabled(userId, characterId);
        boolean isNakedType = characterImageTypeId.equals(UtCharacterImageType.NAKED);
        boolean isPremium = userPlanService.isPremium(userId);
        Character character = characterService.findById(characterId);
        // обрабатываем сообщение
        messageService.createUserTextAskMessage(text, userId, characterId);
        Optional<CharacterImage> notSentImageOptional = characterImageService.findNotSentImage(userId, characterId, characterImageTypeId);
        if(notSentImageOptional.isPresent()) {
            CharacterImage notSentImage = notSentImageOptional.get();
            Message imageMessage = messageService.createCharacterImageMessage(userId, characterId, notSentImage.getId());

            MessageChatItem.MessageChatItemBuilder messageChatItemBuilder = MessageChatItem.builder()
                    .id(imageMessage.getId())
                    .typeId(imageMessage.getTypeId())
                    .imageTypeId(notSentImage.getTypeId())
                    .dtCreate(imageMessage.getDtCreate());

            String imageUrl = minioService.getObjectFromCharacterBucket(notSentImage.getPath());
            if(isPremium || !isNakedType) {
                messageChatItemBuilder.imageUrl(imageUrl);
            }

            if(characterImageTypeId.equals(UtCharacterImageType.NAKED)) {
                String imageBluredUrl = minioService.getObjectFromCharacterBucket(notSentImage.getBluredPath());
                messageChatItemBuilder.imageBluredUrl(imageBluredUrl);
            }
            return new ChatMessageResponseDTO<>(messageChatItemBuilder.build());
        } else {
            String imagePrompt = characterImagePromptService.getPrompt(character, characterImageTypeId);
            Pair<GetImageResponse, ImageGeneration> imageGenerationResponse = imageGenerationService.generate(userId, imagePrompt, character.getTypeId());
            byte[] imageByteArray = Base64.getDecoder().decode(imageGenerationResponse.getFirst().getImage());
            byte[] processedImageByteArray = UtImage.getProcessedImageByteArray(imageByteArray, UtImage.IMAGE_DEFAULT_EXTENSION, UtImage.IMAGE_TARGET_SIZE);
            String path = UtMinio.getCharacterImagePath(characterId) + "/" + UUID.randomUUID() + "." + UtImage.IMAGE_DEFAULT_EXTENSION;
            minioService.putObjectToCharacterBucket(processedImageByteArray, "image/jpeg", path);
            String imageUrl = minioService.getObjectFromCharacterBucket(path);
            MessageChatItem.MessageChatItemBuilder messageChatItemBuilder = MessageChatItem.builder();
            if(isPremium || !isNakedType) {
                messageChatItemBuilder.imageUrl(imageUrl);
            }

            String bluredPath = null;
            if(isNakedType) {
                byte[] bluredImageByteArray = UtImage.getBluredImageByteArray(processedImageByteArray, UtImage.IMAGE_DEFAULT_EXTENSION);
                bluredPath = UtMinio.getCharacterBluredImagePath(characterId) + "/" + UUID.randomUUID();
                minioService.putObjectToCharacterBucket(bluredImageByteArray, "image/jpeg", bluredPath);
                String imageBluredUrl = minioService.getObjectFromCharacterBucket(bluredPath);
                messageChatItemBuilder.imageBluredUrl(imageBluredUrl);
            }
            CharacterImage createdCharacterImage = characterImageService.create(characterId, characterImageTypeId, path, bluredPath, imageGenerationResponse.getSecond().getId());
            Message imageMessage = messageService.createCharacterImageMessage(userId, characterId, createdCharacterImage.getId());


            MessageChatItem result = messageChatItemBuilder
                    .id(imageMessage.getId())
                    .typeId(imageMessage.getTypeId())
                    .imageTypeId(createdCharacterImage.getTypeId())
                    .dtCreate(imageMessage.getDtCreate())
                    .build();
            return new ChatMessageResponseDTO<>(result);
        }
    }

    public void throwIfCharacterNotEnabled(Long userId, Long characterId) {
        Long characterCost = characterService.getCharacterCost(characterId);
        boolean enabled = characterCost == 0 || characterService.getUnlockedCharacterIds(userId).contains(characterId);
        UtError.throwIf(!enabled, new CharacterNotUnlockedException(characterId));
    }


    public GetMessagesResponseDTO getMessagesWithCharacter(Long userId, Long characterId, Integer pageNum, Integer pageSize) {
        CharacterPublicDTO character = characterService.findPublic(characterId);
        SearchResponse<MessageChatItem> content = getMessages(userId, characterId, pageNum, pageSize);
        return new GetMessagesResponseDTO(character, content);
    }

    public SearchResponse<MessageChatItem> getMessages(Long userId, Long characterId, Integer pageNum, Integer pageSize) {
        boolean isPremium = userPlanService.isPremium(userId);
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        Page<Message> page = messageService.getMessages(userId, characterId, pageable);
        List<MessageChatItem> items = page.getContent()
                .stream()
                .map(message -> {
                    Long typeId = message.getTypeId();
                    MessageChatItem messageChatItem = MessageChatItem.builder()
                            .id(message.getId())
                            .typeId(typeId)
                            .text(message.getText())
                            .dtCreate(message.getDtCreate())
                            .build();
                    if(typeId.equals(UtMessageType.AI_IMAGE)) {
                        CharacterImage characterImage = characterImageService.findById(message.getCharacterImageId());
                        messageChatItem.setImageTypeId(characterImage.getTypeId());
                        if(!isPremium && characterImage.getTypeId().equals(UtCharacterImageType.NAKED)) {
                            String url = minioService.getObjectFromCharacterBucket(characterImage.getBluredPath());
                            messageChatItem.setImageBluredUrl(url);
                        } else {
                            String url = minioService.getObjectFromCharacterBucket(characterImage.getPath());
                            messageChatItem.setImageUrl(url);
                        }
                    }
                    return messageChatItem;
                }).collect(Collectors.toList());
        return new SearchResponse<>(items, page.getTotalElements());
    }
}

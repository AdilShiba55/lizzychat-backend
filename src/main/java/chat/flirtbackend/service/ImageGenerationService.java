package chat.flirtbackend.service;

import chat.flirtbackend.dto.FileDTO;
import chat.flirtbackend.dto.GenerateImageDTO;
import chat.flirtbackend.dto.GetImageResponse;
import chat.flirtbackend.dto.UserPlanInfoDTO;
import chat.flirtbackend.entity.ImageGeneration;
import chat.flirtbackend.repository.ImageGenerationRepository;
import chat.flirtbackend.util.UtImage;
import chat.flirtbackend.util.UtMessageType;
import kotlin.Pair;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class ImageGenerationService {

    private final ImageGenerationRepository imageGenerationRepository;
    private final Map<Long, ImageMessageService> imageMessageServices;
    private final UserPlanService userPlanService;
    private final CharacterImagePromptService characterImagePromptService;

    public ImageGenerationService(ImageGenerationRepository imageGenerationRepository, Map<Long, ImageMessageService> imageMessageServices, UserPlanService userPlanService, CharacterImagePromptService characterImagePromptService) {
        this.imageGenerationRepository = imageGenerationRepository;
        this.imageMessageServices = imageMessageServices;
        this.userPlanService = userPlanService;
        this.characterImagePromptService = characterImagePromptService;
    }

    private ImageGeneration create(Long userId, String prompt, GetImageResponse response) {
        ImageGeneration imageGeneration = new ImageGeneration();
        imageGeneration.setUserId(userId);
        imageGeneration.setSeed(response.getSeed());
        imageGeneration.setCost(response.getCost());
        imageGeneration.setDtCreate(new Date());
        imageGeneration.setPrompt(prompt);
        return imageGeneration;
    }

    @Transactional
    public Pair<GetImageResponse, ImageGeneration> generate(Long userId, String prompt, Long characterTypeId) {
        ImageMessageService imageMessageService = imageMessageServices.get(characterTypeId);
        GetImageResponse response = imageMessageService.getCharacterImage(prompt);
        ImageGeneration imageGeneration = create(userId, prompt, response);
        imageGenerationRepository.save(imageGeneration);
        return new Pair<>(response, imageGeneration);
    }

    public long getUserLastDaysImageMessageCount(Long userId) {
        UserPlanInfoDTO plan = userPlanService.getCurrentPlan(userId);
        return imageGenerationRepository.messageCountAfter(userId, plan.getDtStart(), plan.getDtEnd());
    }

    @Transactional
    public FileDTO generateFileDTO(Long userId, String prompt, Long characterTypeId) {
        ImageMessageService imageMessageService = imageMessageServices.get(characterTypeId);
        GetImageResponse response = imageMessageService.getCharacterImage(prompt);
        create(userId, prompt, response);
        byte[] imageByteArray = Base64.getDecoder().decode(response.getImage());
        return new FileDTO(UUID.randomUUID() + ".jpeg", imageByteArray);
    }

    @Transactional
    public FileDTO generateFileDTO(Long userId, GenerateImageDTO generateImage, Long characterImageTypeId) {
        String imagePrompt = characterImagePromptService.getPrompt(generateImage, characterImageTypeId);
        Pair<GetImageResponse, ImageGeneration> imageGenerationResponse = generate(userId, imagePrompt, generateImage.getTypeId());
        byte[] imageByteArray = Base64.getDecoder().decode(imageGenerationResponse.getFirst().getImage());
        return new FileDTO(UUID.randomUUID() + ".jpeg", imageByteArray);
    }
}

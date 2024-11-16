package chat.flirtbackend.controller;

import chat.flirtbackend.dto.FileDTO;
import chat.flirtbackend.dto.GenerateImageDTO;
import chat.flirtbackend.dto.UserPlanInfoDTO;
import chat.flirtbackend.entity.CharacterImage;
import chat.flirtbackend.exception.ApiException;
import chat.flirtbackend.exception.CharacterNotBelongsToUserException;
import chat.flirtbackend.exception.MessageLimitReachedException;
import chat.flirtbackend.service.*;
import chat.flirtbackend.util.UtCharacterImageType;
import chat.flirtbackend.util.UtError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/image")
public class ImageController {

    @Value("${app.message-limit-enabled}")
    private Boolean messageLimitEnabled;
    private final ImageGenerationService imageGenerationService;
    private final UserPlanService userPlanService;
    private final CharacterService characterService;
    private final SystemService systemService;

    public ImageController(ImageGenerationService imageGenerationService, UserPlanService userPlanService, CharacterService characterService, SystemService systemService) {
        this.imageGenerationService = imageGenerationService;
        this.userPlanService = userPlanService;
        this.characterService = characterService;
        this.systemService = systemService;
    }

    @GetMapping("/admin/prompt")
    @Secured("ROLE_admin")
    public synchronized ResponseEntity<FileDTO> generateByPrompt(@RequestParam String prompt, @RequestParam Long characterTypeId) {
        Long userId = systemService.getTokenInfo().getId();
        FileDTO fileDTO = imageGenerationService.generateFileDTO(userId, prompt, characterTypeId);
        return ResponseEntity.ok(fileDTO);
    }

    @GetMapping("/characterForm")
    public synchronized ResponseEntity<FileDTO> generateByCharacterForm(@ModelAttribute @Valid GenerateImageDTO generateImage) {
        Long userId = systemService.getTokenInfo().getId();
        Long characterId = generateImage.getId();

        UserPlanInfoDTO currentPlan = userPlanService.getCurrentPlan(userId);
        long lastMonthImageMessageCount = imageGenerationService.getUserLastDaysImageMessageCount(userId);
        boolean isLimitReached = messageLimitEnabled && (lastMonthImageMessageCount >= currentPlan.getImageMessageLimit());
        UtError.throwIf(isLimitReached, new MessageLimitReachedException("image"));

        boolean notBelongs = characterId != null && !characterService.characterBelongsToUser(characterId, userId);
        UtError.throwIf(notBelongs, new CharacterNotBelongsToUserException());

        boolean isNakedImageNotAllowed = generateImage.getCharacterImageTypeId().equals(UtCharacterImageType.NAKED) && !currentPlan.isPremium();
        UtError.throwIf(isNakedImageNotAllowed, new ApiException("This type of image is not allowed for your current plan"));

        FileDTO file = imageGenerationService.generateFileDTO(userId, generateImage, generateImage.getCharacterImageTypeId());
        return ResponseEntity.ok(file);
    }

}

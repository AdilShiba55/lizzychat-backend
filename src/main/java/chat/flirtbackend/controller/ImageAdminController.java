package chat.flirtbackend.controller;

import chat.flirtbackend.dto.FileDTO;
import chat.flirtbackend.dto.GenerateImageDTO;
import chat.flirtbackend.dto.UserPlanInfoDTO;
import chat.flirtbackend.exception.ApiException;
import chat.flirtbackend.exception.CharacterNotBelongsToUserException;
import chat.flirtbackend.exception.MessageLimitReachedException;
import chat.flirtbackend.service.ImageGenerationService;
import chat.flirtbackend.service.SystemService;
import chat.flirtbackend.util.UtCharacterImageType;
import chat.flirtbackend.util.UtError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/image")
public class ImageAdminController {

    private final ImageGenerationService imageGenerationService;
    private final SystemService systemService;

    public ImageAdminController(ImageGenerationService imageGenerationService, SystemService systemService) {
        this.imageGenerationService = imageGenerationService;
        this.systemService = systemService;
    }

    @GetMapping("/characterForm")
    public synchronized ResponseEntity<FileDTO> generateByCharacterForm(@ModelAttribute @Valid GenerateImageDTO generateImage) {
        Long userId = systemService.getTokenInfo().getId();
        FileDTO file = imageGenerationService.generateFileDTO(userId, generateImage, generateImage.getCharacterImageTypeId());
        return ResponseEntity.ok(file);
    }
}

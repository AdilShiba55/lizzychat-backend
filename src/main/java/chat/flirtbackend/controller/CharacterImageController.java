package chat.flirtbackend.controller;

import chat.flirtbackend.dto.CharacterImageDTO;
import chat.flirtbackend.dto.CharacterImagePublicSearchDTO;
import chat.flirtbackend.dto.FileDTO;
import chat.flirtbackend.dto.SearchResponse;
import chat.flirtbackend.entity.CharacterImage;
import chat.flirtbackend.exception.CharacterNotBelongsToUserException;
import chat.flirtbackend.service.CharacterImageSearchService;
import chat.flirtbackend.service.CharacterImageService;
import chat.flirtbackend.service.CharacterService;
import chat.flirtbackend.service.SystemService;
import chat.flirtbackend.util.UtError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/character_image")
public class CharacterImageController {

    private final CharacterImageSearchService characterImageSearchService;
    private final CharacterImageService characterImageService;
    private final CharacterService characterService;
    private final SystemService systemService;

    public CharacterImageController(CharacterImageSearchService characterImageSearchService, CharacterImageService characterImageService, CharacterService characterService, SystemService systemService) {
        this.characterImageSearchService = characterImageSearchService;
        this.characterImageService = characterImageService;
        this.characterService = characterService;
        this.systemService = systemService;
    }

    @GetMapping("/sent")
    public ResponseEntity<SearchResponse<CharacterImageDTO>> search(@ModelAttribute @Valid CharacterImagePublicSearchDTO filter) {
        Long userId = systemService.getTokenInfo().getId();
        SearchResponse<CharacterImageDTO> response = characterImageSearchService.search(userId, filter);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileDTO> getCharacterImageFile(@PathVariable Long id) {
        CharacterImage characterImage = characterImageService.findById(id);
        Long userId = systemService.getTokenInfo().getId();
        UtError.throwIf(!characterService.characterBelongsToUser(characterImage.getCharacterId(), userId), new CharacterNotBelongsToUserException());
        FileDTO file = characterService.getFileDTO(characterImage.getPath());
        return ResponseEntity.ok(file);
    }
}

package chat.flirtbackend.controller;

import chat.flirtbackend.dto.*;
import chat.flirtbackend.entity.Character;
import chat.flirtbackend.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/character")
public class CharacterAdminController {
    private final CharacterService characterService;
    private final CharacterSearchService characterSearchService;
    private final CharacterImageService characterImageService;
    private final ChatService chatService;
    private final SystemService systemService;
    private final ImageGenerationService imageGenerationService;

    public CharacterAdminController(CharacterService characterService, CharacterSearchService characterSearchService, CharacterImageService characterImageService, ChatService chatService, SystemService systemService, ImageGenerationService imageGenerationService) {
        this.characterService = characterService;
        this.characterSearchService = characterSearchService;
        this.characterImageService = characterImageService;
        this.chatService = chatService;
        this.systemService = systemService;
        this.imageGenerationService = imageGenerationService;
    }

    @PostMapping
    @Secured("ROLE_admin")
    public ResponseEntity<Long> save(@RequestBody @Valid CharacterEditDTO form) {
        Long id = characterService.save(form);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/search")
    @Secured("ROLE_admin")
    public ResponseEntity<SearchResponse<Character>> search(@ModelAttribute @Valid CharacterAdminSearchDTO filter) {
        SearchResponse<Character> searchResponse = characterSearchService.search(filter);
        return ResponseEntity.ok(searchResponse);
    }

    @GetMapping("/{id}")
    @Secured("ROLE_admin")
    public ResponseEntity<CharacterAdminDTO> findById(@PathVariable Long id) {
        CharacterAdminDTO characterAdmin = characterService.findAdminById(id);
        return ResponseEntity.ok(characterAdmin);
    }
}

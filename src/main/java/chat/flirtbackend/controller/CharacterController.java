package chat.flirtbackend.controller;

import chat.flirtbackend.dto.*;
import chat.flirtbackend.exception.ApiException;
import chat.flirtbackend.exception.CharacterNotBelongsToUserException;
import chat.flirtbackend.service.*;
import chat.flirtbackend.util.UtError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/character")
public class CharacterController {
    private final CharacterService characterService;
    private final CharacterSearchService characterSearchService;
    private final SystemService systemService;

    public CharacterController(CharacterService characterService, CharacterSearchService characterSearchService, SystemService systemService) {
        this.characterService = characterService;
        this.characterSearchService = characterSearchService;
        this.systemService = systemService;
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse<CharacterPublicDTO>> search(@ModelAttribute @Valid CharacterPublicSearchDTO filter) {
        SearchResponse<CharacterPublicDTO> result = characterSearchService.search(filter);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/own")
    public ResponseEntity<SearchResponse<CharacterPublicDTO>> findOwn(@ModelAttribute @Valid CharacterOwnSearchDTO filter) {
        Long id = systemService.getTokenInfo().getId();
        SearchResponse<CharacterPublicDTO> response = characterSearchService.search(id, filter);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat")
    public ResponseEntity<SearchResponse<CharacterChatItemDTO>> getCharacterChatItems(@ModelAttribute CharacterChatItemsSearchDTO filter) {
        Long id = systemService.getTokenInfo().getId();
        SearchResponse<CharacterChatItemDTO> characterItems = characterSearchService.getCharacterChatItems(id, filter);
        return ResponseEntity.ok(characterItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterAdminDTO> findById(@PathVariable Long id) {
        Long userId = systemService.getTokenInfo().getId();
        UtError.throwIf(!characterService.characterBelongsToUser(id, userId), new CharacterNotBelongsToUserException());
        CharacterAdminDTO characterAdmin = characterService.findAdminById(id);
        return ResponseEntity.ok(characterAdmin);
    }

    @PostMapping
    public synchronized ResponseEntity<Long> save(@RequestBody @Valid CharacterEditDTO form) {
        if(form.getId() != null) {
            Long userId = systemService.getTokenInfo().getId();
            UtError.throwIf(!characterService.characterBelongsToUser(form.getId(), userId), new CharacterNotBelongsToUserException());
        }
        List<FileDTO> avatars = form.getAvatarFiles();
        UtError.throwIf(avatars != null && avatars.size() > 4, new ApiException("A maximum of 4 images can be uploaded"));

        Long userId = systemService.getTokenInfo().getId();
        form.setUserId(userId);
        form.setCost(0L);
        form.setArchived(false);
        Long id = characterService.save(form);
        return ResponseEntity.ok(id);
    }
}

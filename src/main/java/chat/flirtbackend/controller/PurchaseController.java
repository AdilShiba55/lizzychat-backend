package chat.flirtbackend.controller;

import chat.flirtbackend.dto.UnlockCharacterDTO;
import chat.flirtbackend.service.SystemService;
import chat.flirtbackend.service.UserPurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/purchase")
public class PurchaseController {

    private final UserPurchaseService userPurchaseService;
    private final SystemService systemService;

    public PurchaseController(UserPurchaseService userPurchaseService, SystemService systemService) {
        this.userPurchaseService = userPurchaseService;
        this.systemService = systemService;
    }

    @PostMapping("/unlock-character")
    public ResponseEntity<HttpStatus> unlock(@RequestBody @Valid UnlockCharacterDTO unlockCharacter) {
        Long userId = systemService.getTokenInfo().getId();
        userPurchaseService.unlockCharacter(userId, unlockCharacter.getCharacterId());
        return ResponseEntity.ok().build();
    }
}

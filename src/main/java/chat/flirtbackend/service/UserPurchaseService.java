package chat.flirtbackend.service;

import chat.flirtbackend.entity.Character;
import chat.flirtbackend.entity.UserPurchase;
import chat.flirtbackend.exception.ApiException;
import chat.flirtbackend.exception.NotEnoughCrystalCountException;
import chat.flirtbackend.repository.UserPurchaseRepository;
import chat.flirtbackend.util.UtError;
import chat.flirtbackend.util.UtUserPurchaseType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserPurchaseService {

    private final UserPurchaseRepository userPurchaseRepository;
    private final CharacterService characterService;
    private final UserCrystalService userCrystalService;

    public UserPurchaseService(UserPurchaseRepository userPurchaseRepository, CharacterService characterService, UserCrystalService userCrystalService) {
        this.userPurchaseRepository = userPurchaseRepository;
        this.characterService = characterService;
        this.userCrystalService = userCrystalService;
    }

    private void create(Long userId, Long characterId, Long typeId, Long cost) {
        UserPurchase userPurchase = new UserPurchase();
        userPurchase.setUserId(userId);
        userPurchase.setCharacterId(characterId);
        userPurchase.setTypeId(typeId);
        userPurchase.setCost(cost);
        userPurchase.setDtCreate(new Date());
        userPurchaseRepository.save(userPurchase);
    }

    public void unlockCharacter(Long userId, Long characterId) {
        Character character = characterService.findById(characterId);
        Long currentCrystalCount = userCrystalService.getCurrentCrystalCount(userId);
        boolean isAlreadyUnlocked = getCharacterUnlockedPurchase(userId, characterId).isPresent();
        UtError.throwIf(isAlreadyUnlocked, new ApiException("The character is already unlocked"));
        UtError.throwIf(character.getCost() > currentCrystalCount, new NotEnoughCrystalCountException());
        create(userId, characterId, UtUserPurchaseType.CHARACTER_UNLOCK, character.getCost());
    }

    public Optional<UserPurchase> getCharacterUnlockedPurchase(Long userId, Long characterId) {
        return userPurchaseRepository.getPurchase(userId, characterId, UtUserPurchaseType.CHARACTER_UNLOCK);
    }
}

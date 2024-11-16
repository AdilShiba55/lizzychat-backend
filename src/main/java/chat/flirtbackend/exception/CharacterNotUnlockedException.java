package chat.flirtbackend.exception;

public class CharacterNotUnlockedException extends ApiException {
    public CharacterNotUnlockedException(Long characterId) {
        super("This character(ID:" + characterId + ") is not unlocked");
    }
}

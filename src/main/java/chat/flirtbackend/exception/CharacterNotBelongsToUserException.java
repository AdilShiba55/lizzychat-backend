package chat.flirtbackend.exception;

public class CharacterNotBelongsToUserException extends ApiException {
    public CharacterNotBelongsToUserException() {
        super("The Character doesn't belong to you");
    }
}

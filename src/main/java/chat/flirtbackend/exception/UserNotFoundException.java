package chat.flirtbackend.exception;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(Long id) {
        super("User with this ID not found");
    }
}

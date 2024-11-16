package chat.flirtbackend.exception;

public class MessageLimitReachedException extends ApiException {
    public MessageLimitReachedException(String messageType) {
        super("You have reached your " + messageType + " message limit");
    }
}

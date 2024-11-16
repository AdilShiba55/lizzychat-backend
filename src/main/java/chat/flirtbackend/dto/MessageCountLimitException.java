package chat.flirtbackend.dto;

import chat.flirtbackend.exception.ApiException;

public class MessageCountLimitException extends ApiException {
    public MessageCountLimitException(int limit) {
        super("You have reached your monthly " + limit + " message limit");
    }
}

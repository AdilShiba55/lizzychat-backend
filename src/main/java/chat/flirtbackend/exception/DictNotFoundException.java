package chat.flirtbackend.exception;

public class DictNotFoundException extends ApiException {
    public DictNotFoundException(String dictName) {
        super("Dict " + dictName + " not found");
    }
}

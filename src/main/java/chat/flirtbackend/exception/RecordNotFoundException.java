package chat.flirtbackend.exception;

public class RecordNotFoundException extends ApiException {
    public RecordNotFoundException(Class<?> clazz, Long id) {
        super("Entity " + clazz.getSimpleName() + " with " + id + " id is not found");
    }
}

package chat.flirtbackend.exception;

public class NotEnoughCrystalCountException extends ApiException {

    public NotEnoughCrystalCountException() {
        super("Not enough crystals");
    }
}

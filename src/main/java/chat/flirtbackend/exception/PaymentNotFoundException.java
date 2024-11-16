package chat.flirtbackend.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String paymentId) {
        super("Payment with " + paymentId + " ID is not found");
    }
}

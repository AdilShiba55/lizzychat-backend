package chat.flirtbackend.payment;

public interface PaymentSourceService {
    Long getSourceId();
    PaymentSourceResponseDTO create(Double total, String description);
    void confirm(String paymentId, String payerId);
}

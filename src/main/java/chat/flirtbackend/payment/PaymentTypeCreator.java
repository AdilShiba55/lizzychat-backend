package chat.flirtbackend.payment;

import chat.flirtbackend.entity.MyPayment;

public interface PaymentTypeCreator {
    Double getTotal();
    String getDescription();
    MyPayment create(Long userId, String paymentId, Long sourceId);
}

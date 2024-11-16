package chat.flirtbackend.payment;

import chat.flirtbackend.entity.MyPayment;

public interface PaymentTypeConfirmer {
    MyPayment confirm(String paymentId, String payerId, String token, Long sourceId);
}

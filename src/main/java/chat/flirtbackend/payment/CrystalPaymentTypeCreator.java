package chat.flirtbackend.payment;

import chat.flirtbackend.entity.MyPayment;
import chat.flirtbackend.service.PaymentService;
import chat.flirtbackend.service.UserCrystalService;
import chat.flirtbackend.util.UtPaymentType;
import lombok.Getter;

public class CrystalPaymentTypeCreator implements PaymentTypeCreator {

    @Getter
    private final Double total;
    private final Long crystalCount;
    private final PaymentService paymentService;

    public CrystalPaymentTypeCreator(Double total, Long crystalCount, PaymentService paymentService) {
        this.total = total;
        this.crystalCount = crystalCount;
        this.paymentService = paymentService;
    }

    @Override
    public String getDescription() {
        return "crystal: " + crystalCount;
    }

    @Override
    public MyPayment create(Long userId, String paymentId, Long sourceId) {
        CrystalPaymentDataDTO data = new CrystalPaymentDataDTO(crystalCount);
        return paymentService.create(userId, paymentId, total, sourceId, data, UtPaymentType.CRYSTAL);
    }
}

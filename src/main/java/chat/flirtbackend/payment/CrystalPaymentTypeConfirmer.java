package chat.flirtbackend.payment;

import chat.flirtbackend.entity.MyPayment;
import chat.flirtbackend.service.PaymentService;
import chat.flirtbackend.service.UserCrystalService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class CrystalPaymentTypeConfirmer implements PaymentTypeConfirmer {

    private final PaymentService paymentService;
    private final UserCrystalService userCrystalService;
    private final ObjectMapper objectMapper;

    public CrystalPaymentTypeConfirmer(PaymentService paymentService, UserCrystalService userCrystalService, ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.userCrystalService = userCrystalService;
        this.objectMapper = objectMapper;
    }

    @Override
    public MyPayment confirm(String paymentId, String payerId, String token, Long sourceId) {
        MyPayment myPayment = paymentService.confirm(paymentId, payerId, token, sourceId);
        CrystalPaymentDataDTO data = objectMapper.convertValue(myPayment.getData(), new TypeReference<>() {});
        userCrystalService.create(myPayment.getUserId(), data.getCrystalCount(), myPayment.getId());
        return myPayment;
    }
}

package chat.flirtbackend.payment;

import chat.flirtbackend.entity.MyPayment;
import chat.flirtbackend.service.PaymentService;
import chat.flirtbackend.service.UserCrystalService;
import chat.flirtbackend.service.UserPlanService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class PlanPaymentTypeConfirmer implements PaymentTypeConfirmer {
    private final PaymentService paymentService;
    private final UserPlanService userPlanService;
    private final ObjectMapper objectMapper;

    public PlanPaymentTypeConfirmer(PaymentService paymentService, UserPlanService userPlanService, ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.userPlanService = userPlanService;
        this.objectMapper = objectMapper;
    }

    @Override
    public MyPayment confirm(String paymentId, String payerId, String token, Long sourceId) {
        MyPayment myPayment = paymentService.confirm(paymentId, payerId, token, sourceId);
        PlanPaymentDataDTO data = objectMapper.convertValue(myPayment.getData(), new TypeReference<>() {});
        userPlanService.create(myPayment.getUserId(), data.getPlanId(), data.getMonthCount(), myPayment.getId());
        return myPayment;
    }
}

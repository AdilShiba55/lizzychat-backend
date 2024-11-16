package chat.flirtbackend.payment;

import chat.flirtbackend.entity.MyPayment;
import chat.flirtbackend.service.PaymentService;
import chat.flirtbackend.service.UserPlanService;
import chat.flirtbackend.util.UtPaymentType;
import lombok.Getter;

public class PlanPaymentTypeCreator implements PaymentTypeCreator {

    private final Long planId;
    @Getter
    private final Double total;
    private final Integer monthCount;
    private final PaymentService paymentService;
    private final UserPlanService userPlanService;

    public PlanPaymentTypeCreator(Long planId, Double total, Integer monthCount, PaymentService paymentService, UserPlanService userPlanService) {
        this.planId = planId;
        this.total = total;
        this.monthCount = monthCount;
        this.paymentService = paymentService;
        this.userPlanService = userPlanService;
    }

    @Override
    public String getDescription() {
        return "plan: " + planId;
    }

    @Override
    public MyPayment create(Long userId, String paymentId, Long sourceId) {
        PlanPaymentDataDTO data = new PlanPaymentDataDTO(planId, 1);
        return paymentService.create(userId, paymentId, total, sourceId, data, UtPaymentType.PLAN);
    }
}

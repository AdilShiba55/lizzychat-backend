package chat.flirtbackend.config;

import chat.flirtbackend.payment.*;
import chat.flirtbackend.service.PaymentService;
import chat.flirtbackend.service.UserCrystalService;
import chat.flirtbackend.service.UserPlanService;
import chat.flirtbackend.util.UtPaymentSource;
import chat.flirtbackend.util.UtPaymentType;
import chat.flirtbackend.util.UtPlan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentConfig {

    private final PaymentSourceService paypalPaymentSourceService;
    private final PlanPaymentTypeConfirmer planPaymentTypeConfirmer;
    private final CrystalPaymentTypeConfirmer crystalPaymentTypeConfirmer;
    private final PaymentService paymentService;
    private final UserPlanService userPlanService;
    private final UserCrystalService userCrystalService;

    public PaymentConfig(PaymentSourceService paypalPaymentSourceService, PlanPaymentTypeConfirmer planPaymentTypeConfirmer, CrystalPaymentTypeConfirmer crystalPaymentTypeConfirmer, PaymentService paymentService, UserPlanService userPlanService, UserCrystalService userCrystalService) {
        this.paypalPaymentSourceService = paypalPaymentSourceService;
        this.planPaymentTypeConfirmer = planPaymentTypeConfirmer;
        this.crystalPaymentTypeConfirmer = crystalPaymentTypeConfirmer;
        this.paymentService = paymentService;
        this.userPlanService = userPlanService;
        this.userCrystalService = userCrystalService;
    }

    @Bean
    public Map<Long, PaymentSourceService> paymentSourceServices() {
        return Map.of(UtPaymentSource.PAYPAL, paypalPaymentSourceService);
    }

    @Bean
    public Map<Long, PaymentTypeCreator> planPaymentTypeServices() {
        return Map.of(
                UtPaymentType.PLAN, new PlanPaymentTypeCreator(UtPlan.PREMIUM, 7.99,  1, paymentService, userPlanService)
        );
    }

    @Bean
    public Map<Long, PaymentTypeCreator> crystalPaymentTypeServices() {
        return Map.of(
                200L, new CrystalPaymentTypeCreator(4.99, 200L, paymentService),
                1000L, new CrystalPaymentTypeCreator(19.99, 1000L, paymentService),
                4000L, new CrystalPaymentTypeCreator(49.99, 4000L, paymentService)
        );
    }

    @Bean
    public Map<Long, PaymentTypeConfirmer> paymentTypeConfirmers() {
        return Map.of(
                UtPaymentType.PLAN, planPaymentTypeConfirmer,
                UtPaymentType.CRYSTAL, crystalPaymentTypeConfirmer
        );
    }
}

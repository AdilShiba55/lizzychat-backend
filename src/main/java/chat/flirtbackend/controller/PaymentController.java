package chat.flirtbackend.controller;

import chat.flirtbackend.dto.ConfirmPaymentDTO;
import chat.flirtbackend.payment.PaymentSourceService;
import chat.flirtbackend.payment.PaymentTypeCreator;
import chat.flirtbackend.service.SystemService;
import chat.flirtbackend.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final Map<Long, PaymentSourceService> paymentSourceServices;
    private final Map<Long, PaymentTypeCreator> planPaymentTypeServices;
    private final Map<Long, PaymentTypeCreator> crystalPaymentTypeServices;
    private final PaymentService paymentService;
    private final SystemService systemService;

    public PaymentController(Map<Long, PaymentSourceService> paymentSourceServices, Map<Long, PaymentTypeCreator> planPaymentTypeServices, Map<Long, PaymentTypeCreator> crystalPaymentTypeServices, PaymentService paymentService, SystemService systemService) {
        this.paymentSourceServices = paymentSourceServices;
        this.planPaymentTypeServices = planPaymentTypeServices;
        this.crystalPaymentTypeServices = crystalPaymentTypeServices;
        this.paymentService = paymentService;
        this.systemService = systemService;
    }

    @PostMapping("/{sourceId}/crystal/{count}")
    public ResponseEntity<String> createBalancePayment(@PathVariable Long sourceId, @PathVariable Long count) {
        Long userId = systemService.getTokenInfo().getId();
        PaymentSourceService paymentSourceService = paymentSourceServices.get(sourceId);
        PaymentTypeCreator paymentTypeCreator = crystalPaymentTypeServices.get(count);
        String url = paymentService.create(userId, paymentSourceService, paymentTypeCreator);
        return ResponseEntity.ok(url);
    }

    @PostMapping("/{sourceId}/plan/{planId}")
    public ResponseEntity<String> createPlanPayment(@PathVariable Long sourceId, @PathVariable Long planId) {
        Long userId = systemService.getTokenInfo().getId();
        PaymentSourceService paymentSourceService = paymentSourceServices.get(sourceId);
        PaymentTypeCreator paymentTypeCreator = planPaymentTypeServices.get(planId);
        String url = paymentService.create(userId, paymentSourceService, paymentTypeCreator);
        return ResponseEntity.ok(url);
    }

    @PostMapping("/confirm/{sourceId}")
    public ResponseEntity<HttpStatus> confirm(@PathVariable Long sourceId, @RequestBody @Valid ConfirmPaymentDTO confirmPayment) {
        PaymentSourceService paymentSourceService = paymentSourceServices.get(sourceId);
        paymentService.confirm(confirmPayment, paymentSourceService);
        return ResponseEntity.ok().build();
    }

}

package chat.flirtbackend.service;

import chat.flirtbackend.dto.ConfirmPaymentDTO;
import chat.flirtbackend.entity.MyPayment;
import chat.flirtbackend.exception.ApiException;
import chat.flirtbackend.exception.PaymentNotFoundException;
import chat.flirtbackend.payment.PaymentSourceResponseDTO;
import chat.flirtbackend.payment.PaymentSourceService;
import chat.flirtbackend.payment.PaymentTypeConfirmer;
import chat.flirtbackend.payment.PaymentTypeCreator;
import chat.flirtbackend.repository.PaymentRepository;
import chat.flirtbackend.util.UtError;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final Map<Long, PaymentTypeConfirmer> paymentTypeConfirmers;

    public PaymentService(PaymentRepository paymentRepository, @Lazy Map<Long, PaymentTypeConfirmer> paymentTypeConfirmers) {
        this.paymentRepository = paymentRepository;
        this.paymentTypeConfirmers = paymentTypeConfirmers;
    }

    public MyPayment findByPaymentIdAndSourceId(String paymentId, Long paymentSourceId) {
        return paymentRepository.findByPaymentIdAndSourceId(paymentId, paymentSourceId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    @Transactional
    public String create(Long userId, PaymentSourceService paymentSourceService, PaymentTypeCreator paymentTypeCreator) {
        Double total = paymentTypeCreator.getTotal();
        String description = paymentTypeCreator.getDescription();
        PaymentSourceResponseDTO sourceResponse = paymentSourceService.create(total, description);
        paymentTypeCreator.create(userId,sourceResponse.getPaymentId(), paymentSourceService.getSourceId());
        return sourceResponse.getUrl();
    }

    @Transactional
    public MyPayment confirm(ConfirmPaymentDTO confirmPayment, PaymentSourceService paymentSourceService) {
        Long sourceId = paymentSourceService.getSourceId();
        String paymentId = confirmPayment.getPaymentId();
        String payerId = confirmPayment.getPayerId();
        String token = confirmPayment.getToken();
        paymentSourceService.confirm(paymentId, payerId);
        MyPayment myPayment = findByPaymentIdAndSourceId(paymentId, sourceId);
        PaymentTypeConfirmer paymentTypeConfirmer =  paymentTypeConfirmers.get(myPayment.getTypeId());
        return paymentTypeConfirmer.confirm(paymentId, payerId, token, sourceId);
    }

    public MyPayment create(Long userId, String paymentId, Double total, Long sourceId, Object data, Long paymentTypeId) {
        MyPayment myPayment = new MyPayment();
        myPayment.setUserId(userId);
        myPayment.setTypeId(paymentTypeId);
        myPayment.setPaymentId(paymentId);
        myPayment.setTotal(total);
        myPayment.setSourceId(sourceId);
        myPayment.setData(data);
        myPayment.setDtCreate(new Date());
        return paymentRepository.save(myPayment);
    }

    public MyPayment confirm(String paymentId, String payerId, String token, Long sourceId) {
        MyPayment myPayment = findByPaymentIdAndSourceId(paymentId, sourceId);
        UtError.throwIf(myPayment.getDtConfirmed() != null, new ApiException("payment " + paymentId + " is already confirmed"));
        myPayment.setPayerId(payerId);
        myPayment.setToken(token);
        myPayment.setDtConfirmed(new Date());
        return paymentRepository.save(myPayment);
    }
}

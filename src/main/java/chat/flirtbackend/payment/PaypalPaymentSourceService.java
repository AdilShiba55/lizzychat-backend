package chat.flirtbackend.payment;

import chat.flirtbackend.exception.ApiException;
import chat.flirtbackend.util.UtError;
import chat.flirtbackend.util.UtPaymentSource;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;

@Service
public class PaypalPaymentSourceService implements PaymentSourceService {

    @Value("${app.frontend.url}")
    private String frontendUrl;
    @Value("${app.frontend.payment-success-page}")
    private String frontendPaymentSuccessUrl;
    private final APIContext apiContext;

    public PaypalPaymentSourceService(APIContext apiContext) {
        this.apiContext = apiContext;
    }

    @Override
    public Long getSourceId() {
        return UtPaymentSource.PAYPAL;
    }

    @Override
    @SneakyThrows
    public PaymentSourceResponseDTO create(Double total, String description) {
        Amount amount = new Amount();
        String currency = "USD";
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", total));
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        Payer payer = new Payer();
        payer.setPaymentMethod("Paypal");
        Payment paypalPayment = new Payment();
        paypalPayment.setIntent("sale");
        paypalPayment.setPayer(payer);
        paypalPayment.setTransactions(List.of(transaction));
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(frontendPaymentSuccessUrl + "/" + getSourceId());
        redirectUrls.setCancelUrl(frontendUrl);
        paypalPayment.setRedirectUrls(redirectUrls);
        paypalPayment = paypalPayment.create(apiContext);

        String url = paypalPayment.getLinks()
                .stream()
                .filter(link -> link.getRel().equals("approval_url"))
                .findAny().get()
                .getHref();

        return new PaymentSourceResponseDTO(paypalPayment.getId(), url);
    }

    @Override
    @SneakyThrows
    @Transactional
    public void confirm(String paymentId, String payerId) {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        payment = payment.execute(apiContext, paymentExecution);
        UtError.throwIf(!payment.getState().equals("approved"), new ApiException("Paypal payment is not approved"));
    }
}

package chat.flirtbackend.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentSourceResponseDTO {
    private String paymentId;
    private String url;
}

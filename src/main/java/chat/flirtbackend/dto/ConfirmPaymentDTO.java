package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ConfirmPaymentDTO {
    @NotBlank
    private String paymentId;
    private String payerId;
    private String token;
}

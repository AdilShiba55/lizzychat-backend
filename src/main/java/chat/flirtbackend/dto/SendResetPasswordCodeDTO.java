package chat.flirtbackend.dto;

import chat.flirtbackend.util.UtPattern;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SendResetPasswordCodeDTO {
    @Pattern(regexp = UtPattern.EMAIL_PATTERN, message = UtPattern.EMAIL_PATTERN_MSG)
    private String email;
}

package chat.flirtbackend.dto;

import chat.flirtbackend.util.UtPattern;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ResetPasswordDTO {
    @Pattern(regexp = UtPattern.EMAIL_PATTERN, message = UtPattern.EMAIL_PATTERN_MSG)
    private String email;
    @NotBlank
    @Pattern(regexp = UtPattern.PASSWORD_PATTERN, message = UtPattern.PASSWORD_PATTERN_MSG)
    private String password;
    @Size(min = 6, max = 6)
    private String code;
}

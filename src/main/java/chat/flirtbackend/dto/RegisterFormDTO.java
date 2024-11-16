package chat.flirtbackend.dto;

import chat.flirtbackend.util.UtPattern;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterFormDTO {
    @NotBlank
    @Size(min = 5, max = 50)
    private String username;
    @Pattern(regexp = UtPattern.EMAIL_PATTERN, message = UtPattern.EMAIL_PATTERN_MSG)
    private String email;
    @NotBlank
    @Pattern(regexp = UtPattern.PASSWORD_PATTERN, message = UtPattern.PASSWORD_PATTERN_MSG)
    private String password;
}

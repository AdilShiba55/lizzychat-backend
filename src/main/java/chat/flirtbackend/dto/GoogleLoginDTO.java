package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GoogleLoginDTO {
    @NotBlank
    private String accessToken;
}

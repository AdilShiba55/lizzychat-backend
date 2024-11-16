package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SendImageMessageDTO {
    @NotBlank
    @Size(max = 185)
    private String message;
    @NotNull
    private Long characterId;
    @NotNull
    private Long characterImageTypeId;
}

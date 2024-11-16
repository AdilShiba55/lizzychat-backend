package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserAdminEditDTO {
    @NotNull
    private Long id;
    @NotBlank
    private String username;
    private Boolean blocked;
}

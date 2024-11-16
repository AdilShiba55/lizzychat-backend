package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CharacterAdminSearchDTO {
    private String text;
    private Boolean archived;
    private Boolean imageOnly;
    @NotNull
    private Boolean systemOnly;
    @NotNull
    private Integer pageNum;
    @NotNull
    private Integer pageSize;
}

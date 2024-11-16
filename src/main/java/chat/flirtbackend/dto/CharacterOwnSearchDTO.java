package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CharacterOwnSearchDTO {
    @NotNull
    private Integer pageNum;
    @NotNull
    private Integer pageSize;
}
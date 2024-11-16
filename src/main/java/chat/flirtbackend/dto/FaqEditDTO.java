package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class FaqEditDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Boolean archived;
}

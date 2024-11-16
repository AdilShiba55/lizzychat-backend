package chat.flirtbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FieldErrorDTO {
    private String name;
    private String error;
    private Boolean condition = true;

    public FieldErrorDTO(String name, String error) {
        this.name = name;
        this.error = error;
    }
}

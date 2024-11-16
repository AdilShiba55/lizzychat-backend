package chat.flirtbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageTextTypeIdDTO {
    private String text;
    private Long typeId;
}

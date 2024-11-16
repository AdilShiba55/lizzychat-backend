package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactRequestSearchDTO {
    private String text;
    private Boolean archived;
    private Integer pageNum;
    private Integer pageSize;
}

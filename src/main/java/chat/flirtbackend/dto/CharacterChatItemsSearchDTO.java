package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterChatItemsSearchDTO {
    private String text;
    private Integer pageNum;
    private Integer pageSize;
}

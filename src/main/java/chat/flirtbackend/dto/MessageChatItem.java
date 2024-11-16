package chat.flirtbackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class MessageChatItem {
    private Long id;
    private Long typeId;
    private String text;
    private Long imageTypeId;
    private String imageUrl;
    private String imageBluredUrl;
    private Date dtCreate;
}

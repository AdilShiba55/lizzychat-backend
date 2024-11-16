package chat.flirtbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterChatItemDTO {
    private Long id;
    private String name;
    private String text;
    private String avatarPath;
    private String avatarUrl;
    private Long characterImageId;
}

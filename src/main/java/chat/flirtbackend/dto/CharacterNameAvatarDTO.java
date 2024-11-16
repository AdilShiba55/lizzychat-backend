package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterNameAvatarDTO {
    private String name;
    private String avatarPath;
    private String avatarUrl;
}

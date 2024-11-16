package chat.flirtbackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CharacterImageDTO {
    private Long id;
    private Long characterId;
    private Long typeId;
    private String imageUrl;
    private String imageBluredUrl;


}

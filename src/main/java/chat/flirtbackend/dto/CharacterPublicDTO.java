package chat.flirtbackend.dto;

import chat.flirtbackend.entity.Occupation;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CharacterPublicDTO {
    private Long id;
    private String name;
    private String description;
    private Long age;
    private Long cost;
    private Long typeId;
    private Long ethnicityId;
    private Long raceId;
    private Occupation occupation;
    private List<String> avatarUrls;
    private Date dtCreate;
    private Date dtArchive;
}

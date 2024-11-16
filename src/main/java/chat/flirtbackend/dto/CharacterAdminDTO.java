package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CharacterAdminDTO {
    private Long id;
    private String name;
    private String description;
    private Long age;
    private Long cost;
    private Long typeId;
    private List<FileDTO> avatarFiles;
    private Date dtCreate;
    private Date dtArchive;
    private Long genderId;
    private Long hairStyleId;
    private Long hairColorId;
    private Long eyesColorId;
    private Long breastSizeId;
    private Long ethnicityId;
    private Long bodyTypeId;
    private Long ageLookId;
    private Long occupationId;
    private Long personalityId;
    private Long raceId;
    private Long clothesColorId;
    private Boolean pregnant;
    private Boolean withGlasses;
    private Long dayTimeId;
}

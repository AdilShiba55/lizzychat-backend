package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
public class CharacterEditDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 500)
    private String description;
    @NotNull
    @Min(18)
    @Max(9999)
    private Long age;
    @NotNull
    private Long cost;
    @NotNull
    private Long typeId;
    @NotNull
    private Boolean archived;
    @NotNull
    private Long genderId;
    @NotNull
    private Long hairColorId;
    @NotNull
    private Long hairStyleId;
    @NotNull
    private Long eyesColorId;
    @NotNull
    private Long breastSizeId;
    @NotNull
    private Long ethnicityId;
    @NotNull
    private Long ageLookId;
    @NotNull
    private Long occupationId;
    @NotNull
    private Long bodyTypeId;
    @NotNull
    private Long raceId;
    @NotNull
    private Long personalityId;
    @NotNull
    private Boolean pregnant;
    @NotNull
    private Boolean withGlasses;
    private Long clothesColorId;
    private Long dayTimeId;
    private Long userId;
    private List<FileDTO> avatarFiles;
}

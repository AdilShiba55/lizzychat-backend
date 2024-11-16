package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class GenerateImageDTO {
    private Long id;
    @NotNull
    private Long typeId;
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
    private Boolean pregnant;
    @NotNull
    private Boolean withGlasses;
    private Long clothesId;
    private Long clothesColorId;
    @NotNull
    private Long characterImageTypeId;
    private Long dayTimeId;
    private Long moodId;
    private Long locationId;
}


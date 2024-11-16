package chat.flirtbackend.service;

import chat.flirtbackend.dto.GenerateImageDTO;
import chat.flirtbackend.entity.*;
import chat.flirtbackend.entity.Character;
import chat.flirtbackend.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CharacterImagePromptService {
    private final CharacterGenderService characterGenderService;
    private final EthnicityService ethnicityService;
    private final EyesColorService eyesColorService;
    private final HairColorService hairColorService;
    private final HairStyleService hairStyleService;
    private final BreastSizeService breastSizeService;
    private final AgeLookService ageLookService;
    private final BodyTypeService bodyTypeService;
    private final OccupationService occupationService;
    private final LocationService locationService;
    private final ClothesService clothesService;
    private final RaceService raceService;
    private final ClothesColorService clothesColorService;
    private final DayTimeService dayTimeService;
    private final ObjectMapper objectMapper;

    public CharacterImagePromptService(CharacterGenderService characterGenderService, EthnicityService ethnicityService, EyesColorService eyesColorService, HairColorService hairColorService, HairStyleService hairStyleService, BreastSizeService breastSizeService, AgeLookService ageLookService, BodyTypeService bodyTypeService, OccupationService occupationService, LocationService locationService, ClothesService clothesService, RaceService raceService, ClothesColorService clothesColorService, DayTimeService dayTimeService, ObjectMapper objectMapper) {
        this.characterGenderService = characterGenderService;
        this.ethnicityService = ethnicityService;
        this.eyesColorService = eyesColorService;
        this.hairColorService = hairColorService;
        this.hairStyleService = hairStyleService;
        this.breastSizeService = breastSizeService;
        this.ageLookService = ageLookService;
        this.bodyTypeService = bodyTypeService;
        this.occupationService = occupationService;
        this.locationService = locationService;
        this.clothesService = clothesService;
        this.raceService = raceService;
        this.clothesColorService = clothesColorService;
        this.dayTimeService = dayTimeService;
        this.objectMapper = objectMapper;
    }

    public String getPrompt(Character character, Long imageTypeId) {
        GenerateImageDTO generateImage = objectMapper.convertValue(character, GenerateImageDTO.class);
        return getPrompt(generateImage, imageTypeId);
    }

    public String getPrompt(GenerateImageDTO generateImage, Long imageTypeId) {
        boolean isRoutine = imageTypeId.equals(UtCharacterImageType.ROUTINE);
        boolean isUnderwear = imageTypeId.equals(UtCharacterImageType.UNDERWEAR);
        boolean isNaked = imageTypeId.equals(UtCharacterImageType.NAKED);

        CharacterGender gender = characterGenderService.findById(generateImage.getGenderId());
        Ethnicity ethnicity = ethnicityService.findById(generateImage.getEthnicityId());
        AgeLook ageLook = ageLookService.findById(generateImage.getAgeLookId());
        EyesColor eyesColor = eyesColorService.findById(generateImage.getEyesColorId());
        HairColor hairColor = hairColorService.findById(generateImage.getHairColorId());
        HairStyle hairStyle = hairStyleService.findById(generateImage.getHairStyleId());
        Occupation occupation = occupationService.findById(generateImage.getOccupationId());
        BodyType bodyType = bodyTypeService.findById(generateImage.getBodyTypeId());
        StringBuilder builder = new StringBuilder();
        if(!generateImage.getRaceId().equals(UtRace.HUMAN)) {
            Race race = raceService.findById(generateImage.getRaceId());
            builder.append(race.getName()).append(" ");
        }
        builder.append(ageLook.getName()).append(" ").append(gender.getName()).append(",");
        builder.append(ethnicity.getName()).append(" ethnicity,");
        builder.append(hairColor.getName()).append(" ").append(hairStyle.getName()).append(" hair,");
        builder.append(eyesColor.getName()).append(" eyes,");

        if(generateImage.getWithGlasses()) {
            builder.append(" glasses,");
        }

        builder.append(bodyType.getName()).append(",");

        Long clothesColorId = generateImage.getClothesColorId();
        if(isRoutine) {
            if(clothesColorId != null) {
                ClothesColor clothesColor = clothesColorService.findById(clothesColorId);
                builder.append(clothesColor.getName()).append(" ");
            }
            Long routineClothesId = occupation.getRoutineClothesId();
            Long clothesId = generateImage.getClothesId();
            if(clothesId != null) {
                routineClothesId = clothesId;
            } else if(routineClothesId.equals(UtClothes.RANDOM)) {
                routineClothesId = UtClothes.getRandomClothesId();
            }

            Clothes routineClothes = clothesService.findById(routineClothesId);
            builder.append(routineClothes.getName()).append(",");
        } else if(isUnderwear) {
            if(clothesColorId != null) {
                ClothesColor clothesColor = clothesColorService.findById(clothesColorId);
                builder.append(clothesColor.getName()).append(" ");
            }
            builder.append(" brassiere panties,");
        } else if(isNaked) {
            builder.append(" naked,");
        }

        if(!isRoutine) {
            BreastSize breastSize = breastSizeService.findById(generateImage.getBreastSizeId());
            builder.append(breastSize.getName()).append(" chest,");
        }

        if(generateImage.getPregnant()) {
            builder.append("pregnant,");
        }

        Long moodId = generateImage.getMoodId();
        if(moodId != null) {
            String moodName = UtMood.getMoodName(moodId);
            builder.append(moodName).append(",");
        }

        Long dayTimeId = generateImage.getDayTimeId();
        if(dayTimeId != null) {
            DayTime dayTime = dayTimeService.findById(dayTimeId);
            builder.append(dayTime.getName()).append(",");
        }

        Long locationId = occupation.getLocationIdByImageTypeId(imageTypeId);
        Long specialLocationId = generateImage.getLocationId();
        if(specialLocationId != null) {
            locationId = specialLocationId;
        }
        Location location = locationService.findById(locationId);
        builder.append(location.getName()).append(",");
        return builder.toString();
    }
}

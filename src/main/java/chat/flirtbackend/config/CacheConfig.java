package chat.flirtbackend.config;

import chat.flirtbackend.dto.MinioCacheUrlDTO;
import chat.flirtbackend.entity.*;
import chat.flirtbackend.repository.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class CacheConfig {

    private final CharacterGenderRepository characterGenderRepository;
    private final CharacterTypeRepository characterTypeRepository;
    private final EthnicityRepository ethnicityRepository;
    private final AgeLookRepository ageLookRepository;
    private final EyesColorRepository eyesColorRepository;
    private final HairColorRepository hairColorRepository;
    private final HairStyleRepository hairStyleRepository;
    private final BodyTypeRepository bodyTypeRepository;
    private final OccupationRepository occupationRepository;
    private final ClothesColorRepository clothesColorRepository;
    private final BreastSizeRepository breastSizeRepository;
    private final LocationRepository locationRepository;
    private final RaceRepository raceRepository;
    private final DayTimeRepository dayTimeRepository;

    public CacheConfig(CharacterGenderRepository characterGenderRepository, CharacterTypeRepository characterTypeRepository, EthnicityRepository ethnicityRepository, AgeLookRepository ageLookRepository, EyesColorRepository eyesColorRepository, HairColorRepository hairColorRepository, HairStyleRepository hairStyleRepository, BodyTypeRepository bodyTypeRepository, OccupationRepository occupationRepository, ClothesColorRepository clothesColorRepository, BreastSizeRepository breastSizeRepository, LocationRepository locationRepository, RaceRepository raceRepository, DayTimeRepository dayTimeRepository) {
        this.characterGenderRepository = characterGenderRepository;
        this.characterTypeRepository = characterTypeRepository;
        this.ethnicityRepository = ethnicityRepository;
        this.ageLookRepository = ageLookRepository;
        this.eyesColorRepository = eyesColorRepository;
        this.hairColorRepository = hairColorRepository;
        this.hairStyleRepository = hairStyleRepository;
        this.bodyTypeRepository = bodyTypeRepository;
        this.occupationRepository = occupationRepository;
        this.clothesColorRepository = clothesColorRepository;
        this.breastSizeRepository = breastSizeRepository;
        this.locationRepository = locationRepository;
        this.raceRepository = raceRepository;
        this.dayTimeRepository = dayTimeRepository;
    }

    @Bean
    public ConcurrentHashMap<String, MinioCacheUrlDTO> minioObjectUrlCache() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ConcurrentHashMap<Long, CharacterGender> characterGenderCache() {
        ConcurrentHashMap<Long, CharacterGender> result = new ConcurrentHashMap<>();
        List<CharacterGender> genders = characterGenderRepository.findAll();
        genders.forEach(item -> result.put(item.getId(), item));
        return result;
    }

    @Bean
    public ConcurrentHashMap<Long, CharacterType> characterTypeCache() {
        ConcurrentHashMap<Long, CharacterType> result = new ConcurrentHashMap<>();
        List<CharacterType> characterTypes = characterTypeRepository.findAll();
        characterTypes.forEach(item -> result.put(item.getId(), item));
        return result;
    }

    @Bean
    public ConcurrentHashMap<Long, Ethnicity> ethnicityCache() {
        ConcurrentHashMap<Long, Ethnicity> result = new ConcurrentHashMap<>();
        List<Ethnicity> ethnicities = ethnicityRepository.findAll();
        ethnicities.forEach(item -> result.put(item.getId(), item));
        return result;
    }

    @Bean
    public ConcurrentHashMap<Long, AgeLook> ageLookCache() {
        ConcurrentHashMap<Long, AgeLook> result = new ConcurrentHashMap<>();
        List<AgeLook> ageLooks = ageLookRepository.findAll();
        ageLooks.forEach(item -> result.put(item.getId(), item));
        return result;
    }

    @Bean
    public ConcurrentHashMap<Long, BodyType> bodyTypeCache() {
        ConcurrentHashMap<Long, BodyType> result = new ConcurrentHashMap<>();
        List<BodyType> bodyTypes = bodyTypeRepository.findAll();
        bodyTypes.forEach(item -> result.put(item.getId(), item));
        return result;
    }

    @Bean
    public ConcurrentHashMap<Long, HairColor> hairColorCache() {
        ConcurrentHashMap<Long, HairColor> result = new ConcurrentHashMap<>();
        List<HairColor> hairColors = hairColorRepository.findAll();
        hairColors.forEach(item -> result.put(item.getId(), item));
        return result;
    }

    @Bean
    public ConcurrentHashMap<Long, HairStyle> hairStyleCache() {
        ConcurrentHashMap<Long, HairStyle> result = new ConcurrentHashMap<>();
        List<HairStyle> hairStyles = hairStyleRepository.findAll();
        hairStyles.forEach(item -> result.put(item.getId(), item));
        return result;
    }

    @Bean
    public ConcurrentHashMap<Long, EyesColor> eyesColorCache() {
        ConcurrentHashMap<Long, EyesColor> result = new ConcurrentHashMap<>();
        List<EyesColor> eyesColors = eyesColorRepository.findAll();
        eyesColors.forEach(item -> result.put(item.getId(), item));
        return result;
    }

    @Bean
    public ConcurrentHashMap<Long, Occupation> occupationCache() {
        ConcurrentHashMap<Long, Occupation> result = new ConcurrentHashMap<>();
        List<Occupation> occupations = occupationRepository.findAll();
        occupations.forEach(item -> result.put(item.getId(), item));
        return result;
    }

    @Bean
    public ConcurrentHashMap<Long, ClothesColor> clothesColorCache() {
        ConcurrentHashMap<Long, ClothesColor> result = new ConcurrentHashMap<>();
        List<ClothesColor> clothesColors = clothesColorRepository.findAll();
        clothesColors.forEach(item -> result.put(item.getId(), item));
        return result;
    }

    @Bean
    public ConcurrentHashMap<Long, BreastSize> breastSizeCache() {
        ConcurrentHashMap<Long, BreastSize> result = new ConcurrentHashMap<>();
        List<BreastSize> breastSizes = breastSizeRepository.findAll();
        breastSizes.forEach(item -> result.put(item.getId(), item));
        return result;
    }

    @Bean
    public ConcurrentHashMap<Long, Location> locationCache() {
        ConcurrentHashMap<Long, Location> result = new ConcurrentHashMap<>();
        List<Location> locations = locationRepository.findAll();
        locations.forEach(item -> result.put(item.getId(), item));
        return result;
    }

    @Bean
    public ConcurrentHashMap<Long, Race> raceCache() {
        ConcurrentHashMap<Long, Race> result = new ConcurrentHashMap<>();
        List<Race> races = raceRepository.findAll();
        races.forEach(item -> result.put(item.getId(), item));
        return result;
    }

    @Bean
    public ConcurrentHashMap<Long, DayTime> dayTimeCache() {
        ConcurrentHashMap<Long, DayTime> result = new ConcurrentHashMap<>();
        List<DayTime> dayTimes = dayTimeRepository.findAll();
        dayTimes.forEach(item -> result.put(item.getId(), item));
        return result;
    }
}

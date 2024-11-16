package chat.flirtbackend.service;

import chat.flirtbackend.dto.*;
import chat.flirtbackend.entity.*;
import chat.flirtbackend.entity.Character;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.CharacterRepository;
import chat.flirtbackend.util.UtCharacterImageType;
import chat.flirtbackend.util.UtFile;
import chat.flirtbackend.util.UtImage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final CharacterImageService characterImageService;
    private final OccupationService occupationService;
    private final MinioService minioService;
    private final ObjectMapper objectMapper;

    public CharacterService(CharacterRepository characterRepository, CharacterImageService characterImageService, OccupationService occupationService, MinioService minioService, ObjectMapper objectMapper) {
        this.characterRepository = characterRepository;
        this.characterImageService = characterImageService;
        this.occupationService = occupationService;
        this.minioService = minioService;
        this.objectMapper = objectMapper;
    }

    public void save(Character character) {
        characterRepository.save(character);
    }

    @Transactional
    public void createReferenceCharacter(Long userId) {
        characterRepository.getReferenceCharacter().ifPresent(referenceCharacter -> {
            Character character = objectMapper.convertValue(referenceCharacter, Character.class);
            character.setId(null);
            character.setDtReference(null);
            character.setUserId(userId);
            character.setDtCreate(new Date());
            save(character);
            List<CharacterImage> characterImages = characterImageService.findByCharacterIdAndTypeId(referenceCharacter.getId(), UtCharacterImageType.AVATAR);
            characterImages.forEach(referenceCharacterImage -> {
                CharacterImage characterImage = objectMapper.convertValue(referenceCharacterImage, CharacterImage.class);
                characterImage.setId(null);
                characterImage.setCharacterId(character.getId());
                characterImage.setDtCreate(new Date());
                characterImageService.save(characterImage);
            });
        });
    }

    @Transactional
    public Long save(CharacterEditDTO form) {
        Character character = new Character();
        Long id = form.getId();
        if(id != null) {
            character = findById(id);
        }
        character.setName(form.getName());
        character.setDescription(form.getDescription());
        character.setAge(form.getAge());
        character.setCost(form.getCost());
        character.setTypeId(form.getTypeId());
        character.setDtCreate(new Date());
        character.setGenderId(form.getGenderId());
        character.setHairStyleId(form.getHairStyleId());
        character.setHairColorId(form.getHairColorId());
        character.setEyesColorId(form.getEyesColorId());
        character.setBreastSizeId(form.getBreastSizeId());
        character.setEthnicityId(form.getEthnicityId());
        character.setAgeLookId(form.getAgeLookId());
        character.setBodyTypeId(form.getBodyTypeId());
        character.setUserId(form.getUserId());
        character.setRaceId(form.getRaceId());
        character.setOccupationId(form.getOccupationId());
        character.setPersonalityId(form.getPersonalityId());
        character.setPregnant(form.getPregnant());
        character.setWithGlasses(form.getWithGlasses());
        character.setClothesColorId(form.getClothesColorId());
        character.setDayTimeId(form.getDayTimeId());

        if(!form.getArchived()) {
            character.setDtArchive(null);
        } else if(character.getDtArchive() == null) {
            character.setDtArchive(new Date());
        }

        save(character);
        characterImageService.uploadAvatars(form.getAvatarFiles(), character.getId(), UtImage.IMAGE_TARGET_SIZE);

        return character.getId();
    }

    public Character findById(Long id) {
        return characterRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(Character.class, id));
    }

    public CharacterPublicDTO findPublic(Long id) {
        Character character = findById(id);
        CharacterPublicDTO characterPublic = objectMapper.convertValue(character, CharacterPublicDTO.class);
        Occupation occupation = occupationService.findById(character.getOccupationId());
        characterPublic.setOccupation(occupation);
        List<String> avatarPaths = characterImageService.getImages(characterPublic.getId(), UtCharacterImageType.AVATAR);
        List<String> avatarUrls = minioService.getObjectFromCharacterBucket(avatarPaths);
        characterPublic.setAvatarUrls(avatarUrls);
        return characterPublic;
    }

    public CharacterAdminDTO findAdminById(Long id) {
        Character character = findById(id);
        CharacterAdminDTO characterAdmin = objectMapper.convertValue(character, new TypeReference<>() {});
        characterAdmin.setAvatarFiles(getFiles(id, UtCharacterImageType.AVATAR));
        return characterAdmin;
    }

    private List<FileDTO> getFiles(Long id, Long characterImageTypeId) {
        List<String> avatarPaths = characterImageService.getImages(id, characterImageTypeId);
        List<FileDTO> result = new ArrayList<>();
        for(String avatarPath : avatarPaths) {
            try {
                FileDTO file = getFileDTO(avatarPath);
                result.add(file);
            } catch (Exception ignored) {}
        }
        return result;
    }

    public FileDTO getFileDTO(String path) {
        String name = path.substring(path.lastIndexOf("/") + 1);
        byte[] content = minioService.getFileByteFromCharacterBucket(path);
        return new FileDTO(name, content);
    }

    public List<Long> getUnlockedCharacterIds(Long userId) {
        return characterRepository.getUnlockedCharacterIds(userId);
    }

    public Long getCharacterCost(Long characterId) {
        return characterRepository.getCharacterCost(characterId);
    }

    public boolean characterBelongsToUser(Long characterId, Long userId) {
        return characterRepository.getCharacterCountByUser(characterId, userId) > 0;
    }
}

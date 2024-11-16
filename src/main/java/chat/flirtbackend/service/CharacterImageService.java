package chat.flirtbackend.service;

import chat.flirtbackend.dto.FileDTO;
import chat.flirtbackend.dto.GetImageResponse;
import chat.flirtbackend.entity.Character;
import chat.flirtbackend.entity.CharacterImage;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.CharacterImageRepository;
import chat.flirtbackend.util.UtCharacterImageType;
import chat.flirtbackend.util.UtFile;
import chat.flirtbackend.util.UtImage;
import chat.flirtbackend.util.UtMinio;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CharacterImageService {

    private final CharacterImageRepository characterImageRepository;
    private final MinioService minioService;

    public CharacterImageService(CharacterImageRepository characterImageRepository, MinioService minioService) {
        this.characterImageRepository = characterImageRepository;
        this.minioService = minioService;
    }

    public CharacterImage findById(Long id) {
        return characterImageRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(CharacterImage.class, id));
    }

    public Optional<CharacterImage> findNotSentImage(Long userId, Long characterId, Long characterImageTypeId) {
        return characterImageRepository.findNotSentImage(userId, characterId, characterImageTypeId);
    }

    public CharacterImage save(CharacterImage characterImage) {
        return characterImageRepository.save(characterImage);
    }

    public CharacterImage create(Long characterId, Long typeId, String path) {
        CharacterImage characterImage = new CharacterImage();
        characterImage.setCharacterId(characterId);
        characterImage.setTypeId(typeId);
        characterImage.setPath(path);
        characterImage.setDtCreate(new Date());
        return save(characterImage);
    }

    public CharacterImage create(Long characterId, Long typeId, String path, String bluredPath, Long imageGenerationId) {
        CharacterImage characterImage = new CharacterImage();
        characterImage.setCharacterId(characterId);
        characterImage.setTypeId(typeId);
        characterImage.setPath(path);
        characterImage.setBluredPath(bluredPath);
        characterImage.setGenerationId(imageGenerationId);
        characterImage.setDtCreate(new Date());
        return save(characterImage);
    }

    public List<String> getImages(Long characterId, Long characterImageTypeId) {
        return characterImageRepository.getImages(characterId, characterImageTypeId);
    }

    public List<CharacterImage> findByCharacterIdAndTypeId(Long characterId, Long characterImageTypeId) {
        return characterImageRepository.findByCharacterIdAndTypeId(characterId, characterImageTypeId);
    }

    @Transactional
    public void uploadAvatars(List<FileDTO> files, Long characterId, Integer targetSize) {
        Long characterImageType = UtCharacterImageType.AVATAR;
        List<CharacterImage> characterImages = findByCharacterIdAndTypeId(characterId, characterImageType);
        List<String> referenceCharacterImagePath = characterImageRepository.getReferenceCharacterImagePath();
        for(CharacterImage characterImage : characterImages) {
            String path = characterImage.getPath();
            if(!referenceCharacterImagePath.contains(path)) {
                minioService.removeObjectFromCharacterBucket(path);
            }
            characterImageRepository.delete(characterImage);
        }
        if(files != null) {
            for(FileDTO file : files) {
                String extension = UtFile.getExtension(file);
                String path = UtMinio.getCharacterAvatarImagePath(characterId) + "/" + UUID.randomUUID() + "." + extension;
                byte[] processedImageByteArray = UtImage.getProcessedImageByteArray(file.getContent(), extension, targetSize);
                minioService.putObjectToCharacterBucket(processedImageByteArray, "image/jpeg", path);
                create(characterId, characterImageType, path);
            }
        }
    }
}

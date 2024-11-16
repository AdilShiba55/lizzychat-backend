package chat.flirtbackend.service;

import chat.flirtbackend.dto.FileDTO;
import chat.flirtbackend.dto.PlaygroundImageDTO;
import chat.flirtbackend.entity.HairStyle;
import chat.flirtbackend.entity.PlaygroundImage;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.PlaygroundImageRepository;
import chat.flirtbackend.util.UtFile;
import chat.flirtbackend.util.UtImage;
import chat.flirtbackend.util.UtMinio;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlaygroundImageService {

    private final PlaygroundImageRepository playgroundImageRepository;
    private final MinioService minioService;

    public PlaygroundImageService(PlaygroundImageRepository playgroundImageRepository, MinioService minioService) {
        this.playgroundImageRepository = playgroundImageRepository;
        this.minioService = minioService;
    }

    public PlaygroundImage findById(Long id) {
        return playgroundImageRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(PlaygroundImage.class, id));
    }

    public List<PlaygroundImageDTO> findAll() {
        return playgroundImageRepository.findAllIdDesc().stream().map(item -> {
            String url = minioService.getObjectFromCharacterBucket(item.getPath());
            return PlaygroundImageDTO.builder()
                    .id(item.getId())
                    .path(item.getPath())
                    .url(url)
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional
    public PlaygroundImage create(FileDTO fileDTO) {
        String extension = UtFile.getExtension(fileDTO);
        String path = UtMinio.getPlaygroundImagePath() + "/" + UUID.randomUUID() + "." + extension;
//        byte[] processedImageByteArray = UtImage.getProcessedImageByteArray(fileDTO.getContent(), extension,UtImage.IMAGE_TARGET_SIZE);
        minioService.putObjectToCharacterBucket(fileDTO.getContent(), "image/jpeg", path);
        PlaygroundImage playgroundImage = new PlaygroundImage();
        playgroundImage.setPath(path);
        playgroundImage.setDtCreate(new Date());
        return playgroundImageRepository.save(playgroundImage);
    }

    @Transactional
    public void delete(Long id) {
        PlaygroundImage playgroundImage = findById(id);
        minioService.removeObjectFromCharacterBucket(playgroundImage.getPath());
        playgroundImageRepository.deleteById(id);
    }
}

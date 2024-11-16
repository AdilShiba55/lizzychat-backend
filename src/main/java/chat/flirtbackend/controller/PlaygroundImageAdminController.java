package chat.flirtbackend.controller;

import chat.flirtbackend.dto.ResizeImageDTO;
import chat.flirtbackend.dto.FileDTO;
import chat.flirtbackend.dto.PlaygroundImageDTO;
import chat.flirtbackend.entity.PlaygroundImage;
import chat.flirtbackend.service.MinioService;
import chat.flirtbackend.service.PlaygroundImageService;
import chat.flirtbackend.util.UtFile;
import chat.flirtbackend.util.UtImage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/playground_image")
public class PlaygroundImageAdminController {

    private final PlaygroundImageService playgroundImageService;
    private final MinioService minioService;

    public PlaygroundImageAdminController(PlaygroundImageService playgroundImageService, MinioService minioService) {
        this.playgroundImageService = playgroundImageService;
        this.minioService = minioService;
    }

    @GetMapping
    @Secured("ROLE_admin")
    public ResponseEntity<List<PlaygroundImageDTO>> findAll() {
        List<PlaygroundImageDTO> items = playgroundImageService.findAll();
        return ResponseEntity.ok(items);
    }

    @PostMapping
    @Secured("ROLE_admin")
    public ResponseEntity<PlaygroundImage> create(@RequestBody FileDTO fileDTO) {
        PlaygroundImage playgroundImage = playgroundImageService.create(fileDTO);
        return ResponseEntity.ok(playgroundImage);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_admin")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        playgroundImageService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resize")
    @Secured("ROLE_admin")
    public ResponseEntity<ResizeImageDTO> resize(@RequestBody ResizeImageDTO resizeImage) {
        byte[] content = resizeImage.getContent();
        int target = resizeImage.getTarget().intValue();
        if(content == null) {
            PlaygroundImage playgroundImage = playgroundImageService.findById(resizeImage.getPlaygroundImageId());
            String path = playgroundImage.getPath();
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            content = minioService.getFileByteFromCharacterBucket(playgroundImage.getPath());
            resizeImage.setContent(content);
            resizeImage.setName(fileName);
        }
        byte[] processedImageByteArray = UtImage.getProcessedImageByteArray(content, UtImage.IMAGE_DEFAULT_EXTENSION, target);
        resizeImage.setContent(processedImageByteArray);
        return ResponseEntity.ok(resizeImage);
    }
}

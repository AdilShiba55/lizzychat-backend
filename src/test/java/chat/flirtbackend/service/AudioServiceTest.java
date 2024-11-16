package chat.flirtbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AudioServiceTest {

    private final AudioService audioService;

    @Autowired
    public AudioServiceTest(AudioService audioService) {
        this.audioService = audioService;
    }

    @Test
    void getAudio() {
        audioService.getAudioByteArray("What kind of panties you wear now?", "xpxyeefIMcYRvcUmVORl");
    }

    @Test
    void createAudioAndGetPath() {
        String path = audioService.createAudioAndGetPath("What kind of panties you wear now?", "xpxyeefIMcYRvcUmVORl", 1L);
        Assertions.assertNotNull(path);
    }
}
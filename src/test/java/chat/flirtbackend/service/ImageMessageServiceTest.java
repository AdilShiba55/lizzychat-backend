package chat.flirtbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ImageMessageServiceTest {

    private final ImageMessageStableDiffusionService imageMessageStableDiffusionService;

    @Autowired
    public ImageMessageServiceTest(ImageMessageStableDiffusionService imageMessageStableDiffusionService) {
        this.imageMessageStableDiffusionService = imageMessageStableDiffusionService;
    }
}
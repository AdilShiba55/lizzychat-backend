package chat.flirtbackend.config;

import chat.flirtbackend.client.GetImgApiClient;
import chat.flirtbackend.service.ImageMessageEssentialService;
import chat.flirtbackend.service.ImageMessageService;
import chat.flirtbackend.service.ImageMessageStableDiffusionService;
import chat.flirtbackend.util.UtCharacterImageType;
import chat.flirtbackend.util.UtCharacterType;
import chat.flirtbackend.util.UtMessageType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ImageMessageConfig {
    @Value("${app.getimg.api-key}")
    private String apiKey;
    private final GetImgApiClient getImgApiClient;

    public ImageMessageConfig(GetImgApiClient getImgApiClient) {
        this.getImgApiClient = getImgApiClient;
    }

    @Bean
    public Map<Long, ImageMessageService> imageMessageServices() {
        return Map.of(
                UtCharacterType.REAL, new ImageMessageEssentialService(apiKey, "photorealism", getImgApiClient),
                UtCharacterType.ARTISTIC, new ImageMessageEssentialService(apiKey, "art", getImgApiClient),
                UtCharacterType.ANIME, new ImageMessageStableDiffusionService(apiKey, "something-v2-2", getImgApiClient),
                UtCharacterType.ANIMEv2, new ImageMessageEssentialService(apiKey, "anime", getImgApiClient)
        );
    }
}

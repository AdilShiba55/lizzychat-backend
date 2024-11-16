package chat.flirtbackend.service;

import chat.flirtbackend.client.GetImgApiClient;
import chat.flirtbackend.dto.GetImageResponse;
import chat.flirtbackend.dto.GetImageStableDiffusionRequest;
import chat.flirtbackend.dto.GetImgEssentialRequest;
import chat.flirtbackend.entity.Character;
import chat.flirtbackend.entity.CharacterType;
import chat.flirtbackend.util.UtCharacterImageType;
import chat.flirtbackend.util.UtFile;
import chat.flirtbackend.util.UtImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

public class ImageMessageEssentialService implements ImageMessageService {
    private final String apiKey;
    private final String style;
    private final GetImgApiClient getImgApiClient;

    public ImageMessageEssentialService(String apiKey, String style, GetImgApiClient getImgApiClient) {
        this.apiKey = apiKey;
        this.style = style;
        this.getImgApiClient = getImgApiClient;
    }

    public GetImageResponse getCharacterImage(String imagePrompt) {
        GetImgEssentialRequest request = GetImgEssentialRequest.builder()
                .prompt(imagePrompt)
                .style(style)
                .width(1024L)
                .height(1280L)
                .outputFormat(UtImage.IMAGE_DEFAULT_EXTENSION)
                .build();
        return getImage(request);
    }

    public GetImageResponse getImage(GetImgEssentialRequest request) {
        Map<String, String> headers = Map.of("Authorization", "Bearer " + apiKey);
        return getImgApiClient.post("/v1/essential/text-to-image", request, headers, new ParameterizedTypeReference<>() {});
    }
}

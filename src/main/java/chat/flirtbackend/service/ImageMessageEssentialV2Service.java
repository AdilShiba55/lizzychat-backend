package chat.flirtbackend.service;

import chat.flirtbackend.client.GetImgApiClient;
import chat.flirtbackend.dto.GetImageResponse;
import chat.flirtbackend.dto.GetImgEssentialRequest;
import chat.flirtbackend.dto.GetImgEssentialV2Request;
import chat.flirtbackend.util.UtFile;
import chat.flirtbackend.util.UtImage;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.Map;

public class ImageMessageEssentialV2Service implements ImageMessageService {
    private final String apiKey;
    private final String style;
    private final GetImgApiClient getImgApiClient;

    public ImageMessageEssentialV2Service(String apiKey, String style, GetImgApiClient getImgApiClient) {
        this.apiKey = apiKey;
        this.style = style;
        this.getImgApiClient = getImgApiClient;
    }

    public GetImageResponse getCharacterImage(String imagePrompt) {
        GetImgEssentialV2Request request = GetImgEssentialV2Request.builder()
                .prompt(imagePrompt)
                .style(style)
                .aspectRatio("2:3")
                .outputFormat(UtImage.IMAGE_DEFAULT_EXTENSION)
                .build();
        return getImage(request);
    }

    public GetImageResponse getImage(GetImgEssentialV2Request request) {
        Map<String, String> headers = Map.of("Authorization", "Bearer " + apiKey);
        return getImgApiClient.post("/v1/essential-v2/text-to-image", request, headers, new ParameterizedTypeReference<>() {});
    }
}

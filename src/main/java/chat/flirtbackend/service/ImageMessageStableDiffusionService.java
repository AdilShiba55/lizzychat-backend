package chat.flirtbackend.service;

import chat.flirtbackend.client.GetImgApiClient;
import chat.flirtbackend.dto.GetImageStableDiffusionRequest;
import chat.flirtbackend.dto.GetImageResponse;
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

public class ImageMessageStableDiffusionService implements ImageMessageService {

    private final String apiKey;
    private final String modelId;
    private final GetImgApiClient getImgApiClient;

    public ImageMessageStableDiffusionService(String apiKey, String modelId, GetImgApiClient getImgApiClient) {
        this.apiKey = apiKey;
        this.modelId = modelId;
        this.getImgApiClient = getImgApiClient;
    }

    public GetImageResponse getCharacterImage(String imagePrompt) {
        GetImageStableDiffusionRequest request = GetImageStableDiffusionRequest.builder()
                .model(modelId)
                .prompt(imagePrompt)
                .negativePrompt("(octane render, render, drawing, bad photo, bad photography:1.3), (worst quality, low quality, blurry:1.2), (bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers, bad fingers), morbid, mutilated, mutation, disfigured")
//                .width(1024L)
//                .height(1280L)
                .width(512L)
                .height(640L)
                .steps(25L)
                .guidance(9L)
                .outputFormat(UtImage.IMAGE_DEFAULT_EXTENSION)
                .build();
        return getImage(request);
    }

    public GetImageResponse getImage(GetImageStableDiffusionRequest request) {
        Map<String, String> headers = Map.of("Authorization", "Bearer " + apiKey);
        return getImgApiClient.post("/v1/stable-diffusion/text-to-image", request, headers, new ParameterizedTypeReference<>() {});
    }
}

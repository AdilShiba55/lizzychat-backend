package chat.flirtbackend.service;

import chat.flirtbackend.client.EvenlabsApiClient;
import chat.flirtbackend.dto.GetAudioRequest;
import chat.flirtbackend.util.UtMinio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class AudioService {

    @Value("${app.evenlabs.api-key}")
    private String evenlabsApiKey;
    private final EvenlabsApiClient evenlabsApiClient;
    private final MinioService minioService;

    public AudioService(EvenlabsApiClient evenlabsApiClient, MinioService minioService) {
        this.evenlabsApiClient = evenlabsApiClient;
        this.minioService = minioService;
    }

    public byte[] getAudioByteArray(String text, String voiceId) {
        String endpoint = "/v1/text-to-speech/" + voiceId;
        GetAudioRequest getAudioRequest = new GetAudioRequest(text);
        return evenlabsApiClient.post(endpoint, getAudioRequest, Map.of("xi-api-key", evenlabsApiKey), new ParameterizedTypeReference<>() {});
    }

    public String createAudioAndGetPath(String text, String voiceId, Long characterId) {
        String path = UtMinio.getCharacterAudioPath(characterId) + "/" + UUID.randomUUID();
        byte[] audioByteArray = getAudioByteArray(text, voiceId);
        minioService.putObjectToCharacterBucket(audioByteArray, "audio/mpeg", path);
        return path;
    }

    public String createAudioAndGetUrl(String text, String voiceId, Long characterId) {
        String path = UtMinio.getCharacterAudioPath(characterId) + "/" + UUID.randomUUID();
        byte[] audioByteArray = getAudioByteArray(text, voiceId);
        minioService.putObjectToCharacterBucket(audioByteArray, "audio/mpeg", path);
        return minioService.getObjectFromCharacterBucket(path);
    }
}

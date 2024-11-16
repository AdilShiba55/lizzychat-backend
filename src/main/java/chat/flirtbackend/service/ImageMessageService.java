package chat.flirtbackend.service;

import chat.flirtbackend.dto.GetImageResponse;
import chat.flirtbackend.entity.Character;

public interface ImageMessageService {
    GetImageResponse getCharacterImage(String imagePrompt);
}

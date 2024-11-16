package chat.flirtbackend.util;

import java.util.UUID;

public class UtMinio {

    public static String getPlaygroundImagePath() {
        return "/playground";
    }

    public static String getCharacterAvatarImagePath(Long characterId) {
        return characterId + "/avatar";
    }

    public static String getCharacterImagePath(Long characterId) {
        return characterId + "/image";
    }

    public static String getCharacterBluredImagePath(Long characterId) {
        return characterId + "/blured_image";
    }

    public static String getCharacterAudioPath(Long characterId) {
        return characterId + "/audio";
    }
}

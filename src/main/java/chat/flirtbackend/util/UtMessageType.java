package chat.flirtbackend.util;

import java.util.Objects;

public class UtMessageType {
    public static final Long USER_TEXT = 1L;
    public static final Long AI_TEXT = 3L;
    public static final Long AI_VOICE = 4L;
    public static final Long USER_GIFT = 5L;
    public static final Long AI_IMAGE = 6L;
    public static final Long USER_TEXT_ASK = 7L;

    public static boolean isUserText(Long typeId) {
        return Objects.equals(typeId, USER_TEXT);
    }
}

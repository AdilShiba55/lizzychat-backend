package chat.flirtbackend.util;

import java.util.Map;

public class UtMood {
    public static final Long SMILE = 1L;
    public static final Long SHY = 2L;
    public static final Long ANGRY = 3L;

    public static final Map<Long, String> MOODS = Map.of(SMILE, "Smile", SHY, "Shy", ANGRY, "Angry");

    public static String getMoodName(Long id) {
        return MOODS.get(id);
    }
}

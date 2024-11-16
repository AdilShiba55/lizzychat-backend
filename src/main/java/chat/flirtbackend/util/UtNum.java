package chat.flirtbackend.util;

import java.util.Random;
import java.util.UUID;

public class UtNum {

    public static String getRandomCode(int length) {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, length);
    }
}

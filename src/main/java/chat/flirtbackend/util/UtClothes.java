package chat.flirtbackend.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class UtClothes {
    public static final Long DRESS = 4L;
    public static final Long TSHIRT = 5L;
    public static final Long SWEATER = 6L;
    public static final Long JACKET = 7L;
    public static final Long COAT = 8L;
    public static final Long RANDOM = 20L;
    public static final List<Long> randomIds = Arrays.asList(DRESS, TSHIRT, SWEATER, JACKET, COAT);

    public static Long getRandomClothesId() {
        Random random = new Random();
        return randomIds.get(random.nextInt(randomIds.size()));
    }
}

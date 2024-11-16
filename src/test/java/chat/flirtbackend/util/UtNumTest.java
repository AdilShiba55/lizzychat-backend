package chat.flirtbackend.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UtNumTest {

    @Test
    void getRandom() {
        String code = UtNum.getRandomCode(6);
        Assertions.assertNotNull(code);
    }
}
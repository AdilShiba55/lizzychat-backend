package chat.flirtbackend.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

class UtPatternTest {

    @Test
    void testEmailPattern() {
        String email = "adil.bayalinov@gmail.com";
        boolean isEmail = Pattern.matches(UtPattern.EMAIL_PATTERN, email);
        Assertions.assertTrue(isEmail);
        String notEmail = email.replaceAll("@", "");
        isEmail = Pattern.matches(UtPattern.EMAIL_PATTERN, notEmail);
        Assertions.assertFalse(isEmail);
    }

}
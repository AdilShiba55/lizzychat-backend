package chat.flirtbackend.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordEncoderTest {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordEncoderTest(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    void encode() {
        String encoded = passwordEncoder.encode("admin");
        Assertions.assertNotNull(encoded);
    }
}

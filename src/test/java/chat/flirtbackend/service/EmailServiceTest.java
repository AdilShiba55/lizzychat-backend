package chat.flirtbackend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailServiceTest {

    private final EmailService emailService;
    private final UserService userService;

    @Autowired
    public EmailServiceTest(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @Test
    void sendMessage() {
        emailService.sendMessage("adil.bayalinov@mail.ru", "code?", "123456");
    }

    @Test
    void sendVerifyCode() {
        String email = userService.findEmailById(1L);
        Context context = new Context();
        context.setVariable("code", "111222");
        emailService.sendMessage(email, "Password reset", "send-password-reset-code", context);
    }
}
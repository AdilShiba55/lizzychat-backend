package chat.flirtbackend.service;

import chat.flirtbackend.dto.TokenInfoDTO;
import chat.flirtbackend.entity.User;
import chat.flirtbackend.entity.UserVerification;
import chat.flirtbackend.exception.ApiException;
import chat.flirtbackend.exception.FieldException;
import chat.flirtbackend.repository.UserVerificationRepository;
import chat.flirtbackend.security.TokenService;
import chat.flirtbackend.util.UtError;
import chat.flirtbackend.util.UtNum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class UserVerificationService {

    @Value("${app.email.verification-limit}")
    private Integer limit;
    private final UserVerificationRepository userVerificationRepository;
    private final UserService userService;
    private final TokenService tokenService;
    private final EmailService emailService;

    public UserVerificationService(UserVerificationRepository userVerificationRepository, UserService userService, TokenService tokenService, EmailService emailService) {
        this.userVerificationRepository = userVerificationRepository;
        this.userService = userService;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    private void save(String code, Long userId) {
        UserVerification userVerification = new UserVerification(code, userId, new Date());
        userVerificationRepository.save(userVerification);
    }

    public void sendVerificationCode(Long userId) {
        String email = userService.findEmailById(userId);
        Optional<UserVerification> last = userVerificationRepository.findLast(userId);
        if(last.isPresent()) {
            long difference = (new Date().getTime() - last.get().getDtCreate().getTime()) / 1000;
            UtError.throwIf(difference < limit, new ApiException("You can resend the code in " + (limit - difference) + " seconds"));
        }
        String code = UtNum.getRandomCode(6);
        Context context = new Context();
        context.setVariable("code", code);
        emailService.sendMessage(email, "Verify", "send-verify-code", context);
        save(code, userId);
    }

    @Transactional
    public TokenInfoDTO verifyEmail(String code, Long userId) {
        User user = userService.findById(userId);
        ApiException incorrectCodeException = new ApiException("Incorrect code");
        UserVerification userVerification = userVerificationRepository.findLast(userId).orElseThrow(() -> incorrectCodeException);
        UtError.throwIf(user.getDtVerified() != null, new ApiException("Your account email is already verified"));
        boolean codeMatches = code.equalsIgnoreCase(userVerification.getCode());
        UtError.throwIf(!codeMatches, incorrectCodeException);
        user.setDtVerified(new Date());
        userService.save(user);
        userVerificationRepository.removeAll(userId);
        return tokenService.generateTokenInfo(user);
    }
}

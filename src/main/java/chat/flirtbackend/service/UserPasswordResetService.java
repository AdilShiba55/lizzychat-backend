package chat.flirtbackend.service;

import chat.flirtbackend.dto.ResetPasswordDTO;
import chat.flirtbackend.dto.TokenInfoDTO;
import chat.flirtbackend.entity.User;
import chat.flirtbackend.entity.UserPasswordReset;
import chat.flirtbackend.entity.UserVerification;
import chat.flirtbackend.exception.ApiException;
import chat.flirtbackend.repository.UserPasswordResetRepository;
import chat.flirtbackend.security.TokenService;
import chat.flirtbackend.util.UtError;
import chat.flirtbackend.util.UtNum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
public class UserPasswordResetService {

    @Value("${app.email.password-reset-limit}")
    private Integer limit;

    private final UserPasswordResetRepository userPasswordResetRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UserPasswordResetService(UserPasswordResetRepository userPasswordResetRepository, UserService userService, EmailService emailService, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userPasswordResetRepository = userPasswordResetRepository;
        this.userService = userService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    private void save(String code, Long userId) {
        UserPasswordReset userPasswordReset = new UserPasswordReset(code, userId, new Date());
        userPasswordResetRepository.save(userPasswordReset);
    }

    public void sendResetCode(String email) {
        Long userId = userService.findUserIdByEmail(email);
        Optional<UserPasswordReset> last = userPasswordResetRepository.findLast(userId);
        if(last.isPresent()) {
            long difference = (new Date().getTime() - last.get().getDtCreate().getTime()) / 1000;
            UtError.throwIf(difference < limit, new ApiException("You can resend the code in " + (limit - difference) + " seconds"));
        }
        String code = UtNum.getRandomCode(6);
        Context context = new Context();
        context.setVariable("code", code);
        emailService.sendMessage(email, "Password reset", "send-password-reset-code", context);
        save(code, userId);
    }

    @Transactional
    public TokenInfoDTO changePassword(ResetPasswordDTO resetPassword) {
        Long userId = userService.findUserIdByEmail(resetPassword.getEmail());
        ApiException incorrectCodeException = new ApiException("Incorrect code");
        UserPasswordReset userPasswordReset = userPasswordResetRepository.findLast(userId).orElseThrow(() -> incorrectCodeException);
        boolean codeMatches = resetPassword.getCode().equalsIgnoreCase(userPasswordReset.getCode());
        UtError.throwIf(userPasswordReset.getDtConfirmed() != null || !codeMatches, incorrectCodeException);
        userPasswordReset.setDtConfirmed(new Date());
        userPasswordResetRepository.save(userPasswordReset);
        userPasswordResetRepository.removeAll(userPasswordReset.getUserId());
        return changePassword(userId, resetPassword.getPassword());
    }

    public TokenInfoDTO changePassword(Long userId, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = userService.findById(userId);
        user.setPassword(encodedPassword);
        userService.save(user);
        return tokenService.generateTokenInfo(user);
    }
}

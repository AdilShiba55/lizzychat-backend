package chat.flirtbackend.service;

import chat.flirtbackend.dto.FieldErrorDTO;
import chat.flirtbackend.dto.LoginFormDTO;
import chat.flirtbackend.dto.RegisterFormDTO;
import chat.flirtbackend.dto.TokenInfoDTO;
import chat.flirtbackend.entity.User;
import chat.flirtbackend.entity.UserCrystal;
import chat.flirtbackend.entity.UserVerification;
import chat.flirtbackend.exception.ApiException;
import chat.flirtbackend.exception.FieldException;
import chat.flirtbackend.repository.UserRepository;
import chat.flirtbackend.security.TokenService;
import chat.flirtbackend.util.UtError;
import chat.flirtbackend.util.UtNum;
import chat.flirtbackend.util.UtUserSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {
    @Value("${app.register-crystal-count}")
    private Long registerCrystalCount;
    private final TokenService tokenService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserCrystalService userCrystalService;
    private final CharacterService characterService;

    public AuthService(TokenService tokenService, UserService userService, PasswordEncoder passwordEncoder, UserCrystalService userCrystalService, CharacterService characterService) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userCrystalService = userCrystalService;
        this.characterService = characterService;
    }

    @Transactional
    public TokenInfoDTO register(@Valid RegisterFormDTO registerForm) {
        boolean isUsernameBusy = userService.existByUsername(registerForm.getUsername());
        boolean isEmailBusy = userService.existByEmail(registerForm.getEmail());
        UtError.throwIf(
                new FieldErrorDTO("username", "This name is already taken", isUsernameBusy),
                new FieldErrorDTO("email", "This email is already taken", isEmailBusy));
        String encodedPassword = passwordEncoder.encode(registerForm.getPassword());
        User user = User.builder()
                .username(registerForm.getUsername())
                .email(registerForm.getEmail())
                .password(encodedPassword)
                .sourceId(UtUserSource.OWN)
                .dtCreate(new Date())
                .build();
        userService.save(user);
        userCrystalService.create(user.getId(), registerCrystalCount, null);
        characterService.createReferenceCharacter(user.getId());
        return tokenService.generateTokenInfo(user);
    }

    public TokenInfoDTO login(@Valid LoginFormDTO loginForm) {
        User user = userService.findByUsernameOrEmail(loginForm.getUsernameOrEmail());
        boolean isPasswordCorrect = passwordEncoder.matches(loginForm.getPassword(), user.getPassword());
        UtError.throwIf(!isPasswordCorrect, new FieldException("password", "incorrect password"));
        return tokenService.generateTokenInfo(user);
    }

    public TokenInfoDTO refresh(Long userId) {
        User user = userService.findById(userId);
        return tokenService.generateTokenInfo(user);
    }
}

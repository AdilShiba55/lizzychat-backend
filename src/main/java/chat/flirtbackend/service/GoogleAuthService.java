package chat.flirtbackend.service;

import chat.flirtbackend.client.GoogleApiClient;
import chat.flirtbackend.dto.FieldErrorDTO;
import chat.flirtbackend.dto.GoogleRegisterDTO;
import chat.flirtbackend.dto.GoogleUserInfoDTO;
import chat.flirtbackend.dto.TokenInfoDTO;
import chat.flirtbackend.entity.User;
import chat.flirtbackend.exception.ApiException;
import chat.flirtbackend.security.TokenService;
import chat.flirtbackend.util.UtError;
import chat.flirtbackend.util.UtUserSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class GoogleAuthService {

    @Value("${app.register-crystal-count}")
    private Long registerCrystalCount;
    private final UserService userService;
    private final TokenService tokenService;
    private final UserCrystalService userCrystalService;
    private final CharacterService characterService;
    private final GoogleApiClient googleApiClient;
    private final PasswordEncoder passwordEncoder;

    public GoogleAuthService(UserService userService, TokenService tokenService, UserCrystalService userCrystalService, CharacterService characterService, GoogleApiClient googleApiClient, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.userCrystalService = userCrystalService;
        this.characterService = characterService;
        this.googleApiClient = googleApiClient;
        this.passwordEncoder = passwordEncoder;
    }

    public GoogleUserInfoDTO getGoogleUserInfo(String token) {
        String endpoint = "/oauth2/v3/tokeninfo";
        try {
            return googleApiClient.get(endpoint, Map.of("access_token", token), new ParameterizedTypeReference<>() {});
        } catch (Exception exception) {
            throw new ApiException("Something went wrong with google");
        }
    }

    @Transactional
    public TokenInfoDTO register(GoogleRegisterDTO googleRegister) {
        GoogleUserInfoDTO googleUserInfo = getGoogleUserInfo(googleRegister.getToken());
        boolean isUsernameBusy = userService.existByUsername(googleRegister.getUsername());
        boolean isEmailBusy = userService.existByEmail(googleUserInfo.getEmail());
        UtError.throwIf(
                new FieldErrorDTO("username", "This name is already taken", isUsernameBusy),
                new FieldErrorDTO("email", "This email is already taken", isEmailBusy));
        String encodedPassword = passwordEncoder.encode(googleRegister.getPassword());
        User user = User.builder()
                .username(googleRegister.getUsername())
                .email(googleUserInfo.getEmail())
                .password(encodedPassword)
                .sourceId(UtUserSource.GOOGLE)
                .dtCreate(new Date())
                .dtVerified(new Date())
                .build();

        userService.save(user);
        userCrystalService.create(user.getId(), registerCrystalCount, null);
        characterService.createReferenceCharacter(user.getId());
        return tokenService.generateTokenInfo(user);
    }

    public TokenInfoDTO login(String token) {
        GoogleUserInfoDTO googleUserInfo = getGoogleUserInfo(token);
        User user = userService.findByEmail(googleUserInfo.getEmail());
        return tokenService.generateTokenInfo(user);
    }
}

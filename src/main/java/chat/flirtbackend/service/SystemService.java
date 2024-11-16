package chat.flirtbackend.service;

import chat.flirtbackend.dto.TokenInfoDTO;
import chat.flirtbackend.util.UtToken;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
public class SystemService {

    private final Environment environment;

    public SystemService(Environment environment) {
        this.environment = environment;
    }

    public String[] getCurrentProfile() {
        return environment.getActiveProfiles();
    }

    public boolean isProdProfile() {
        return getCurrentProfile().length > 0 && environment.getActiveProfiles()[0].equals("prod");
    }

    public static String getCurrentToken() {
        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes());
        HttpServletRequest request = attributes.getRequest();
        String bearerToken = request.getHeader("Authorization");
        return UtToken.removeBearer(bearerToken);
    }

    public TokenInfoDTO getTokenInfo() {
        return (TokenInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean isTokenPresent() {
        return !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser");
    }

}

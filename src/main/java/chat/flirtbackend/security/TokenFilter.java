package chat.flirtbackend.security;

import chat.flirtbackend.dto.TokenInfoDTO;
import chat.flirtbackend.dto.UserInfoDTO;
import chat.flirtbackend.entity.Role;
import chat.flirtbackend.service.RoleService;
import chat.flirtbackend.service.UserService;
import chat.flirtbackend.util.UtToken;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TokenFilter extends OncePerRequestFilter {

//    private final List<String> allowedWithoutVerification = Arrays.asList("/api/v1/verification", "/api/v1/verification/code", "/api/v1/user/info", "/api/v1/contact_request");
    private final TokenService tokenService;
    private final UserService userService;
    private final RoleService roleService;

    public TokenFilter(TokenService tokenService, UserService userService, RoleService roleService) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeaderIsInvalid(authorizationHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = UtToken.removeBearer(authorizationHeader);
        TokenInfoDTO tokenInfo = tokenService.parse(token);
        boolean isExpired = tokenInfo.isExpired();
        boolean isBlocked = userService.isUserBlocked(tokenInfo.getId());
        if(isExpired || isBlocked) {
            filterChain.doFilter(request, response);
            return;
        }
        List<Role> roles = roleService.getRolesByUserId(tokenInfo.getId());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getAuth(tokenInfo, roles);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);
    }

    private boolean authorizationHeaderIsInvalid(String authorizationHeader) {
        return authorizationHeader == null || !authorizationHeader.startsWith("Bearer ");
    }

    private UsernamePasswordAuthenticationToken getAuth(TokenInfoDTO tokenInfo, List<Role> roles) {
        List<GrantedAuthority> authorities = roles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.getName()))
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(tokenInfo, null, authorities);
    }
}

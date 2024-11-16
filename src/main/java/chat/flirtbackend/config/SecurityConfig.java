package chat.flirtbackend.config;

import chat.flirtbackend.security.AuthEntryPoint;
import chat.flirtbackend.security.TokenFilter;
import chat.flirtbackend.security.TokenService;
import chat.flirtbackend.service.RoleService;
import chat.flirtbackend.service.UserService;
import chat.flirtbackend.util.UtError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final UserService userService;
    private final TokenService tokenService;
    private final RoleService roleService;
    private final AuthEntryPoint authEntryPoint;
    private final CorsConfiguration corsConfiguration;

    public SecurityConfig(UserService userService, TokenService tokenService, RoleService roleService, AuthEntryPoint authEntryPoint, CorsConfiguration corsConfiguration) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.roleService = roleService;
        this.authEntryPoint = authEntryPoint;
        this.corsConfiguration = corsConfiguration;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors().configurationSource(request -> corsConfiguration)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .mvcMatchers(
                        "/api/v1/auth/register", "/api/v1/auth/login", "/api/v1/auth/refresh",
                        "/swagger-ui/**", "/v3/api-docs/**", "/template/**", "/css/**",
                        "/api/v1/character/search", "/api/v1/user-password/**", "/api/v1/user/exists",
                        "/api/v1/google/auth/register", "/api/v1/google/auth/login",
                        "/api/v1/faq/search", "/api/v1/dict/**"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new TokenFilter(tokenService, userService, roleService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(authEntryPoint);

        return http.build();
    }

}

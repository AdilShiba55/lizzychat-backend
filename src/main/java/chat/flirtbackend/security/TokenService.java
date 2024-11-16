package chat.flirtbackend.security;

import chat.flirtbackend.dto.TokenInfoDTO;
import chat.flirtbackend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    public TokenInfoDTO generateTokenInfo(User user) {
        String token = generateStr(user);
        return parse(token);
    }

    public String generateStr(User user) {
        Instant accessTokenExpirationDate = Instant.now().plus(14, ChronoUnit.DAYS);

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        JwtBuilder jwtBuilder = Jwts.builder();
        if(user.getDtVerified() != null) {
            jwtBuilder.claim("dtVerified", new Date());
        }

        return jwtBuilder
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .setExpiration(Date.from(accessTokenExpirationDate))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenInfoDTO parse(String token) {
        byte[] secretBytes = jwtSecret.getBytes();

        Claims claims= Jwts.parserBuilder()
                .setSigningKey(secretBytes)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long id = claims.get("id", Long.class);
        String email = claims.get("email", String.class);
        Date dtVerified = claims.get("dtVerified", Date.class);
        Long exp = claims.get("exp", Long.class);
        return new TokenInfoDTO(id, email, dtVerified, token, exp);
    }
}

package chat.flirtbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class TokenInfoDTO {
    private Long id;
    private String email;
    private Date dtVerified;
    private String token;
    private Long exp;

    public boolean isExpired() {
        Date now = new Date();
        Date expiringDate = new Date(exp * 1000);
        return now.after(expiringDate);
    }
}

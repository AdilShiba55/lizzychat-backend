package chat.flirtbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleUserInfoDTO {
    private String azp;
    private String aud;
    private String sub;
    private String scope;
    private String exp;
    @JsonProperty("expires_in")
    private String expiresIn;
    private String email;
    @JsonProperty("email_verified")
    private String emailVerified;
    @JsonProperty("access_type")
    private String accessType;
}

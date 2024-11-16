package chat.flirtbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetImgEssentialRequest {
    private String prompt;
    private String style;
    private Long width;
    private Long height;
    @JsonProperty("output_format")
    private String outputFormat;
}

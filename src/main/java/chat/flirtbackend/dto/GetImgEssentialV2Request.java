package chat.flirtbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetImgEssentialV2Request {
    private String prompt;
    private String style;
    @JsonProperty("aspect_ratio")
    private String aspectRatio;
    @JsonProperty("output_format")
    private String outputFormat;
}

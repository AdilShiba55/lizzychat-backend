package chat.flirtbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetImageStableDiffusionRequest {
    private String model;
    private String prompt;
    @JsonProperty("negative_prompt")
    private String negativePrompt;
    private Long width;
    private Long height;
    private Long steps;
    private Long guidance;
    @JsonProperty("output_format")
    private String outputFormat;
}

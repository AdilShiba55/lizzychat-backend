package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AiProps {
    private String url;
    private String apiKey;
    private String modelName;
    private Double modelTemperature;
}

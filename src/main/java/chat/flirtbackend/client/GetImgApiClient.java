package chat.flirtbackend.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GetImgApiClient extends ApiClient {

    public GetImgApiClient(@Value("${app.getimg.url}") String getImgUrl, RestTemplate restTemplate) {
        super(getImgUrl, restTemplate);
    }
}

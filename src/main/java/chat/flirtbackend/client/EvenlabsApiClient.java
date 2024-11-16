package chat.flirtbackend.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EvenlabsApiClient extends ApiClient {

    public EvenlabsApiClient(@Value("${app.evenlabs.url}") String evenlabsUrl, RestTemplate restTemplate) {
        super(evenlabsUrl, restTemplate);
    }
}

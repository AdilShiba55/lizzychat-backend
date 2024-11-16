package chat.flirtbackend.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleApiClient extends ApiClient {

    public GoogleApiClient(@Value("${app.google-api-url}") String googleApiUrl, RestTemplate restTemplate) {
        super(googleApiUrl, restTemplate);
    }
}

package chat.flirtbackend.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public abstract class ApiClient {
    private final String baseUrl;
    private final RestTemplate restTemplate;

    public ApiClient(String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    public <T> T get(String endpoint, Map<String, Object> params, ParameterizedTypeReference<T> reference) {
        UriComponentsBuilder builder = getBuilder(endpoint, params);
        ResponseEntity<T> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, reference);
        return response.getBody();
    }

    public <T> T post(String endpoint, Object body, Map<String, String> headers, ParameterizedTypeReference<T> reference) {
        UriComponentsBuilder builder = getBuilder(endpoint, null);
        HttpHeaders httpHeaders = new HttpHeaders();
        if(headers != null) {
            headers.forEach(httpHeaders::add);
        }
        HttpEntity<Object> httpEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<T> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, httpEntity, reference);
        return response.getBody();
    }

    private UriComponentsBuilder getBuilder(String endpoint, Map<String, Object> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + endpoint);
        if (params != null) {
            params.forEach(builder::queryParam);
        }
        return builder;
    }
}

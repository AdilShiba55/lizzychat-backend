package chat.flirtbackend.config;

import chat.flirtbackend.service.MinioService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PrepareConfig {

    private final MinioService minioService;

    public PrepareConfig(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostConstruct
    private void init() {
        minioService.createDirectoryInUserBucket("1");
    }
}

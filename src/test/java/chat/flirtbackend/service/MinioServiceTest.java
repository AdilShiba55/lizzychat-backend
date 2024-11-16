package chat.flirtbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MinioServiceTest {

    private final MinioService minioService;

    @Autowired
    public MinioServiceTest(MinioService minioService) {
        this.minioService = minioService;
    }
}
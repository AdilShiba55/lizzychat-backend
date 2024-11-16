package chat.flirtbackend.config;

import chat.flirtbackend.service.SystemService;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${app.minio.url}")
    private String url;
    @Value("${app.minio.get-presigned-url}")
    private String getPresignedUrl;
    @Value("${app.minio.login}")
    private String login;
    @Value("${app.minio.password}")
    private String password;
    @Autowired
    private SystemService systemService;

    @Bean
    @SneakyThrows
    public MinioClient minioClient() {
        MinioClient.Builder builder = MinioClient.builder()
                .endpoint(url)
                .credentials(login, password);
        MinioClient minioClient = builder.build();
        minioClient.ignoreCertCheck();
        return minioClient;
    }

    @Bean
    @SneakyThrows
    public MinioClient minioPresignedUrlClient() {
        MinioClient.Builder builder = MinioClient.builder()
                .endpoint(getPresignedUrl)
                .credentials(login, password);

        if(systemService.isProdProfile()) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        HttpUrl originalUrl = chain.request().url();
                        HttpUrl newUrl = originalUrl.newBuilder()
                                .encodedPath("/minio" + originalUrl.encodedPath())
                                .build();
                        return chain.proceed(chain.request().newBuilder().url(newUrl).build());
                    }).build();
            builder.httpClient(okHttpClient);
        }

        MinioClient minioClient = builder.build();
        minioClient.ignoreCertCheck();
        return minioClient;
    }
}

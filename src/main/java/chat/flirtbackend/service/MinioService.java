package chat.flirtbackend.service;

import chat.flirtbackend.dto.MinioCacheUrlDTO;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MinioService {

    @Value("${app.minio.get-presigned-url}")
    private String getPresignedUrl;
    @Value("${app.minio.url-live}")
    private Integer urlLive;
    @Value("${app.minio.buckets.users}")
    private String usersBucketName;
    @Value("${app.minio.buckets.characters}")
    private String charactersBucketName;
    private final MinioClient minioClient;
    private final MinioClient minioPresignedUrlClient;
    private final SystemService systemService;
    private final ConcurrentHashMap<String, MinioCacheUrlDTO> minioObjectUrlCache;

    public MinioService(MinioClient minioClient, MinioClient minioPresignedUrlClient, SystemService systemService, ConcurrentHashMap<String, MinioCacheUrlDTO> minioObjectUrlCache) {
        this.minioClient = minioClient;
        this.minioPresignedUrlClient = minioPresignedUrlClient;
        this.systemService = systemService;
        this.minioObjectUrlCache = minioObjectUrlCache;
    }

    @PostConstruct
    private void init() {
        createBucket(usersBucketName);
        createBucket(charactersBucketName);
    }

    public void putObjectToUserBucket(byte[] byteArray, String contentType, String path) {
        putObject(byteArray, contentType, usersBucketName, path);
    }

    public void putObjectToCharacterBucket(byte[] byteArray, String contentType, String path) {
        putObject(byteArray, contentType, charactersBucketName, path);
    }

    @SneakyThrows
    public void putObject(byte[] byteArray, String contentType, String bucketName, String path) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(path)
                .stream(byteArrayInputStream, byteArray.length, -1)
                .contentType(contentType)
                .build();
        minioClient.putObject(args);
    }

    @SneakyThrows
    public void putObject(MultipartFile multipartFile, String bucketName, String path) {
        InputStream inputStream = multipartFile.getInputStream();
        PutObjectArgs args = PutObjectArgs
                .builder()
                .bucket(bucketName)
                .object(path)
                .stream(inputStream, multipartFile.getSize(), -1)
                .build();
        minioClient.putObject(args);
    }

    public String getObjectFromUserBucket(String path) {
        return getObjectUrl(usersBucketName, path);
    }

    public String getObjectFromCharacterBucket(String path) {
        return getObjectUrl(charactersBucketName, path);
    }

    public List<String> getObjectFromUserBucket(List<String> paths) {
        return paths.stream()
                .map(this::getObjectFromUserBucket)
                .collect(Collectors.toList());
    }

    public List<String> getObjectFromCharacterBucket(List<String> paths) {
        return paths.stream()
                .map(this::getObjectFromCharacterBucket)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private String getObjectUrl(String bucketName, String objectName) {
        String key = bucketName + "/" + objectName;
        if(minioObjectUrlCache.containsKey(key)) {
            MinioCacheUrlDTO cacheUrl = minioObjectUrlCache.get(key);
            long differentInMillis = cacheUrl.getDtCreate().getTime() - (new Date()).getTime();
            long differentDays = TimeUnit.DAYS.convert(differentInMillis, TimeUnit.MILLISECONDS);
            if(differentDays < urlLive-2) {
                return cacheUrl.getUrl();
            }
        }
//        StatObjectResponse objectStat = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
        GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(urlLive, TimeUnit.DAYS)
                .build();
        String url = minioPresignedUrlClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
        if(systemService.isProdProfile()) {
            // для nginx, чтобы делал редирект в minio
            url = url.replaceFirst(getPresignedUrl, getPresignedUrl+"/minio");
        }
        minioObjectUrlCache.put(key, new MinioCacheUrlDTO(url, new Date()));
        return url;
    }

    @SneakyThrows
    public byte[] getFileByteFromCharacterBucket(String path) {
        InputStream inputStream = minioClient.getObject(GetObjectArgs
                .builder()
                .bucket(charactersBucketName)
                .object(path)
                .build());
        return StreamUtils.copyToByteArray(inputStream);
    }

    @SneakyThrows
    public String getBase64FromCharacterBucket(String path) {
        InputStream inputStream = minioClient.getObject(GetObjectArgs
                .builder()
                .bucket(charactersBucketName)
                .object(path)
                .build());
        byte[] buffer = inputStream.readAllBytes();
        return Base64.getEncoder().encodeToString(buffer);
    }

    @SneakyThrows
    private void createBucket(String bucketName) {
        if (!isBucketExists(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    @SneakyThrows
    private boolean isBucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    public void createDirectoryInUserBucket(String directoryName) {
        createDirectory(usersBucketName, directoryName);
    }

    public void createDirectoryInCharacterBucket(String directoryName) {
        createDirectory(charactersBucketName, directoryName);
    }

    @SneakyThrows
    public void createDirectory(String bucketName, String directoryName) {
        if (!isDirectoryExist(bucketName, directoryName)) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(directoryName + "/")
                    .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                    .build());
        }
    }

    @SneakyThrows
    public boolean isDirectoryExist(String bucketName, String directoryName) {
        ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder().bucket(bucketName).prefix(directoryName).recursive(false).build();
        Iterable<Result<Item>> results = minioClient.listObjects(listObjectsArgs);
        for (Result<Item> result : results) {
            Item item = result.get();
            if (item.isDir() && directoryName.equals(item.objectName())) {
                return true;
            }
        }
        return false;
    }

    @SneakyThrows
    public void removeObjectFromCharacterBucket(String path) {
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket(charactersBucketName)
                .object(path)
                .build();

        minioClient.removeObject(removeObjectArgs);
    }

}

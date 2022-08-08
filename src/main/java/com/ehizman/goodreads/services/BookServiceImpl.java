package com.ehizman.goodreads.services;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class BookServiceImpl implements BookService{
    private final AmazonS3 amazonS3;
    private final String IMAGE_BUCKET = "good-reads-image-bucket";
    private final String FILE_BUCKET = "good-reads-file-bucket";


    public BookServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    private String generateUrl(String bucketName, String fileName, HttpMethod httpMethod){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 10);
        return amazonS3.generatePresignedUrl(bucketName, fileName, calendar.getTime(), httpMethod).toString();
    }

    @Override
    @Async
    public CompletableFuture<Map<String, String>> generateUploadURLs(String fileExtension, String imageExtension) throws ExecutionException, InterruptedException {
        String fileName = UUID.randomUUID()+fileExtension;
        String imageFileName = UUID.randomUUID()+imageExtension;

        String fileUploadUrl = generateUploadUrlForFile(fileName).get();
        String imageUploadUrl = generateUploadUrlForImage(imageFileName).get();

        Map<String, String> map = new HashMap<>();
        map.put("fileUploadUrl", fileUploadUrl);
        map.put("imageUploadUrl", imageUploadUrl);
        return CompletableFuture.completedFuture(map);
    }

    @Async
    public CompletableFuture<String> generateUploadUrlForImage(String imageFileName) {
        log.info("Generating upload url for image");
        return CompletableFuture.completedFuture(generateUrl(IMAGE_BUCKET, imageFileName, HttpMethod.PUT));
    }

    @Async
    public CompletableFuture<String> generateUploadUrlForFile(String fileName){
        log.info("Generating upload url for file");
        return CompletableFuture.completedFuture(generateUrl(FILE_BUCKET, fileName, HttpMethod.PUT));
    }
}

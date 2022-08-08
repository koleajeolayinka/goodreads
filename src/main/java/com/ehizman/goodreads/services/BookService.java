package com.ehizman.goodreads.services;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface BookService {
    CompletableFuture<Map<String, String>> generateUploadURLs(String fileExtension, String imageExtension) throws ExecutionException, InterruptedException;
}

package com.ehizman.goodreads.services;

import com.ehizman.goodreads.controllers.requestsAndResponses.BookItemUploadRequest;
import com.ehizman.goodreads.exceptions.GoodReadsException;
import com.ehizman.goodreads.models.Book;
import com.ehizman.goodreads.models.Credentials;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface BookService {
    CompletableFuture<Map<String, Credentials>> generateUploadURLs(String fileExtension, String imageExtension) throws ExecutionException, InterruptedException;
    Book save(BookItemUploadRequest bookItemUploadRequest);
    Book findBookByTitle(String title);
    Map<String, String> generateDownloadUrls(String fileName, String imageFileName) throws GoodReadsException;
    Map<String, Object> findAll(int pageNumber, int noOfItems);
}

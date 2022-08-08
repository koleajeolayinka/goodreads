package com.ehizman.goodreads.controllers;

import com.ehizman.goodreads.controllers.requestsAndResponses.ApiResponse;
import com.ehizman.goodreads.exceptions.GoodReadsException;
import com.ehizman.goodreads.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
@RequestMapping("/api/v1/books")
public class BookController {
    private BookService bookService;
    private List<String> validImageExtensions;
    private List<String> validFileExtensions;

    public BookController(BookService bookService) {
        this.bookService = bookService;
        validImageExtensions = Arrays.asList(".png", ".jpg",".jpeg");
        validFileExtensions = Arrays.asList(".txt", ".pdf", ".doc", ".docx", ".csv",
        ".epub", ".xlsx");
    }

    @GetMapping("/upload")
    public ResponseEntity<?> getUploadUrls(
            @RequestParam("fileExtension") @Valid @NotBlank @NotNull String fileExtension,
            @RequestParam("imageExtension") @Valid @NotBlank @NotNull String imageExtension){
        try {
            if (!validFileExtensions.contains(fileExtension)){
                throw new GoodReadsException("file extension not accepted", 400);
            }
            if (!validImageExtensions.contains(imageExtension)){
                throw new GoodReadsException("image extension not accepted", 400);
            }
            Map<String, String> map = bookService.generateUploadURLs(fileExtension, imageExtension).get();
            ApiResponse apiResponse = ApiResponse.builder()
                    .status("success")
                    .message("upload urls created")
                    .data(map)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

        } catch (GoodReadsException | ExecutionException | InterruptedException e) {
            log.info(e.getMessage());
            e.printStackTrace();
            ApiResponse apiResponse = ApiResponse.builder()
                    .status("fail")
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }
}

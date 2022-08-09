package com.ehizman.goodreads.controllers;

import com.ehizman.goodreads.controllers.requestsAndResponses.ApiResponse;
import com.ehizman.goodreads.controllers.requestsAndResponses.BookItemUploadRequest;
import com.ehizman.goodreads.exceptions.GoodReadsException;
import com.ehizman.goodreads.models.Book;
import com.ehizman.goodreads.models.Credentials;
import com.ehizman.goodreads.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/")
    public ResponseEntity<?> uploadBookItem(@RequestBody @Valid @NotNull BookItemUploadRequest bookItemUploadRequest){
        Book book = bookService.save(bookItemUploadRequest);
        ApiResponse apiResponse = ApiResponse.builder()
                .status("success")
                .message("book saved successfully")
                .data(book)
                .build();
        return  new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{pageNo}/{noOfItems}")
    public ResponseEntity<?> getAllBooks(
            @PathVariable(value = "pageNo", required = false) @DefaultValue({"0"}) @NotNull String pageNo,
            @PathVariable(value = "noOfItems", required = false) @DefaultValue({"10"}) @NotNull String numberOfItems){

        Map<String, Object> pageResult = bookService.findAll(Integer.parseInt(pageNo), Integer.parseInt(numberOfItems));
        ApiResponse apiResponse = ApiResponse.builder()
                .status("success")
                .message("page returned")
                .data(pageResult)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getBookByTitle(@RequestParam @NotNull @NotBlank String title){
        Book book = bookService.findBookByTitle(title);
        ApiResponse apiResponse = ApiResponse.builder()
                .status("success")
                .message("book found")
                .data(book)
                .result(1)
                .build();
        return  new ResponseEntity<>(apiResponse, HttpStatus.FOUND);
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
            Map<String, Credentials> map = bookService.generateUploadURLs(fileExtension, imageExtension).get();
            ApiResponse apiResponse = ApiResponse.builder()
                    .status("success")
                    .message("upload urls created")
                    .data(map)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (GoodReadsException | ExecutionException | InterruptedException e) {
            log.info("Exception --> {}", e.getMessage());
            ApiResponse apiResponse = ApiResponse.builder()
                    .status("fail")
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<?> getDownloadUrls(
            @RequestParam("fileName") @Valid @NotBlank @NotNull String fileName,
            @RequestParam("imageFileName") @Valid @NotBlank @NotNull String imageFileName
    ) {
        try {
            if (!validFileExtensions.contains("."+fileName.split("\\.")[1])){
                throw new GoodReadsException("file extension not accepted", 400);
            }
            if (!validImageExtensions.contains("."+imageFileName.split("\\.")[1])){
                throw new GoodReadsException("image file extension not accepted", 400);
            }
            Map<String, String> map = bookService.generateDownloadUrls(fileName, imageFileName);
            ApiResponse apiResponse = ApiResponse.builder()
                    .status("success")
                    .message("download urls created")
                    .data(map)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }
        catch (GoodReadsException e) {
            log.info("Exception --> {}", e.getMessage());
            ApiResponse apiResponse = ApiResponse.builder()
                    .status("fail")
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }
}

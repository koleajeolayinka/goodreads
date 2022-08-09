package com.ehizman.goodreads.respositories;

import com.ehizman.goodreads.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface BookRepository extends MongoRepository<Book, String > {

    Book findBookByTitleIsIgnoreCase(String title);
    @Override
    Page<Book> findAll(Pageable pageable);
}

package com.ehizman.goodreads.respositories;

import com.ehizman.goodreads.models.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String > {
}

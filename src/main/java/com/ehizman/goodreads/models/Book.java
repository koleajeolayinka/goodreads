package com.ehizman.goodreads.models;

import com.ehizman.goodreads.models.enums.AgeRate;
import com.ehizman.goodreads.models.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Document("books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private String id;

    private String title;
    private String author;

    private String description;

    private String coverImageUrl;

    private AgeRate ageRate;

    private String uploadedBy;

    private LocalDateTime dateUploaded;

    private Category category;
}

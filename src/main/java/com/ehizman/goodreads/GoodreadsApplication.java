package com.ehizman.goodreads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GoodreadsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoodreadsApplication.class, args);
	}

}

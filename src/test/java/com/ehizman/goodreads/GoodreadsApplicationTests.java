package com.ehizman.goodreads;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
class GoodreadsApplicationTests {
	@Autowired
	private DataSource dataSource;
	@Test
	void testThatDataSourceExists() {
		assertNotNull(dataSource);
		try{
			Connection connection = dataSource.getConnection();
			assertThat(connection).isNotNull();
			assertThat(connection.getCatalog()).isEqualTo("goodreads");
			log.info("Catalog --> {}", connection.getCatalog());
		}
		catch (SQLException exception){
			log.info("An error occurred --> {}", exception.toString());
		}
	}
}

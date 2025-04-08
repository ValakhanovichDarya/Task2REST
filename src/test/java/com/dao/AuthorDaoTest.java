package com.dao;

import com.model.entity.Author;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthorDaoTest {
    private static final MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.0")
                    .withDatabaseName("testbookdb")
                    .withUsername("test")
                    .withPassword("test")
                    .withInitScript("init.sql");

    private AuthorDao authorDao;

    @BeforeAll
    static void startContainer() {
        mysql.start();
    }

    @AfterAll
    static void stopContainer() {
        mysql.stop();
    }

    @BeforeEach
    void setUp() {
        PoolProperties poolProperties = new PoolProperties();
        poolProperties.setUrl(mysql.getJdbcUrl());
        poolProperties.setDriverClassName("com.mysql.cj.jdbc.Driver");
        poolProperties.setUsername(mysql.getUsername());
        poolProperties.setPassword(mysql.getPassword());

        authorDao = new AuthorDao(poolProperties);

        cleanDatabase();
    }

    private void cleanDatabase() {
        try (Connection connection = authorDao.connectionManager.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM authors;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreateAuthorAndFindById() throws SQLException {
        Author author = new Author("Test Author");
        authorDao.create(author);
        Optional<Author> createdAuthor = authorDao.findById(1L);

        assertTrue(createdAuthor.isPresent());
        assertEquals("Test Author", createdAuthor.get().getName());
    }

    @Test
    void testFindAll() throws SQLException {
        Author author = new Author("Test Author");
        authorDao.create(author);

        List<Author> authors = authorDao.findAll();

        assertFalse(authors.isEmpty());
    }

}


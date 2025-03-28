package com.dao;

import com.model.entity.Book;
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

class BookDaoTest {
    private static final MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.0")
                    .withDatabaseName("testbookdb")
                    .withUsername("test")
                    .withPassword("test")
                    .withInitScript("init.sql");

    private BookDao bookDao;

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

        bookDao = new BookDao(poolProperties);

        cleanDatabase();
    }

    private void cleanDatabase() {
        try (Connection connection = bookDao.connectionManager.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM books;");
            stmt.execute("DELETE FROM authors;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createBook() {
        try (Connection connection = bookDao.connectionManager.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO authors (name) VALUES ('Test Author');");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Author author = new Author(1L, "Test Author");
        Book book = new Book("Test Book", 100, author);

        bookDao.create(book);
    }

    @Test
    void testCreateBook() {
        createBook();
        Optional<Book> createdBook = bookDao.findById(1L);

        assertTrue(createdBook.isPresent());
        assertEquals("Test Book", createdBook.get().getName());
    }

    @Test
    void testFindAll() {
        createBook();

        List<Book> books = bookDao.findAll();

        assertFalse(books.isEmpty());
    }

    @Test
    void testFindById() {
        createBook();

        Optional<Book> book = bookDao.findById(1L);

        assertTrue(book.isPresent());
        assertEquals("Test Book", book.get().getName());
    }

    @Test
    void testUpdateBook() {
        createBook();
        Optional<Book> bookOptional = bookDao.findById(1L);
        assertTrue(bookOptional.isPresent());
        Book book = bookOptional.get();
        book.setName("Updated Book");
        book.setNumberOfPages(400);

        bookDao.update(book);

        Optional<Book> updatedBook = bookDao.findById(1L);
        assertTrue(updatedBook.isPresent());
        assertEquals("Updated Book", updatedBook.get().getName());
        assertEquals(400, updatedBook.get().getNumberOfPages());
    }

    @Test
    void testDeleteBook() {
        createBook();
        Optional<Book> bookOptional = bookDao.findById(1L);
        assertTrue(bookOptional.isPresent());

        bookDao.delete(bookOptional.get());

        Optional<Book> deletedBook = bookDao.findById(1L);
        assertFalse(deletedBook.isPresent());
    }
}

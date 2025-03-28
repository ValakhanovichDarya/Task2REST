package com.services;

import com.dao.AuthorDao;
import com.model.entity.Author;
import com.model.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    @Mock
    private AuthorDao authorDao;

    @Mock
    private BookService bookService;

    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authorService = new AuthorService(authorDao, bookService);
    }

    @Test
    void createNewAuthor() {
        Author author = new Author(1, "Author One");

        authorService.createNewAuthor(author);

        verify(authorDao, times(1)).create(author); // Проверяем, что метод create был вызван один раз
    }

    @Test
    void findAllAuthors() {

        Author author1 = new Author(1, "Author One");
        Author author2 = new Author(2, "Author Two");

        List<Author> authors = Arrays.asList(author1, author2);
        List<Book> books1 = Arrays.asList(new Book(1, "Book One", 100, author1), new Book(2, "Book Two", 200, author1));
        List<Book> books2 = Collections.singletonList(new Book(3, "Book Three", 150, author2));

        when(authorDao.findAll()).thenReturn(authors);
        when(bookService.findByAuthor(author1)).thenReturn(books1);
        when(bookService.findByAuthor(author2)).thenReturn(books2);

        List<Author> result = authorService.findAll();

        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getBooks().size());
        assertEquals(1, result.get(1).getBooks().size());

        verify(bookService, times(1)).findByAuthor(author1);
        verify(bookService, times(1)).findByAuthor(author2);
    }
}
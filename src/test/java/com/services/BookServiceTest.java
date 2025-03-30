package com.services;

import com.dao.BookDao;
import com.model.entity.Author;
import com.model.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookDao bookDao;

    @InjectMocks
    private BookService bookService;

    private Author author;
    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        author = new Author(1, "Test Author");
        book = new Book(1, "Book One", 100, author);
    }

    @Test
    void createNewBook() throws SQLException {
        bookService.createNewBook(book);

        verify(bookDao, times(1)).create(book);
    }

    @Test
    void findByAuthor() throws SQLException {

        Book book2 = new Book(2, "Book Two", 200, author);

        List<Book> expectedBooks = Arrays.asList(book, book2);
        when(bookDao.findByAuthorId(author.getId())).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.findByAuthor(author);

        verify(bookDao, times(1)).findByAuthorId(author.getId());
        assert actualBooks.size() == 2;
        assert actualBooks.contains(book);
        assert actualBooks.contains(book2);
    }

    @Test
    void updateBook() throws SQLException {
        bookService.updateBook(book);

        verify(bookDao, times(1)).update(book);
    }

    @Test
    void deleteBook() throws SQLException{
        bookService.deleteBook(book);

        verify(bookDao, times(1)).delete(book);
    }
}

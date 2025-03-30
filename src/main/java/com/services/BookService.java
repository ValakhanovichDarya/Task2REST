package com.services;

import com.dao.BookDao;
import com.model.entity.Author;
import com.model.entity.Book;

import java.sql.SQLException;
import java.util.List;

public class BookService {
    private static BookService INSTANCE = null;
    private BookDao bookDao;

    private BookService() {
        bookDao = BookDao.getInstance();
    }

    public static BookService getInstance() {
        if (INSTANCE == null) {
            synchronized (BookService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BookService();
                }
            }
        }
        return INSTANCE;
    }

    public void createNewBook(Book book) throws SQLException {
        bookDao.create(book);
    }

    public List<Book> findByAuthor(Author author) throws SQLException {
        return bookDao.findByAuthorId(author.getId());
    }

    public void updateBook(Book book) throws SQLException {
        bookDao.update(book);
    }

    public void deleteBook(Book book) throws SQLException {
        bookDao.delete(book);
    }
}

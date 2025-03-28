package com.services;

import com.dao.BookDao;
import com.model.entity.Author;
import com.model.entity.Book;

import java.util.List;

public class BookService {
    private static BookService INSTANCE = null;
    private BookDao bookDao;

    private BookService() {
        bookDao = BookDao.getInstance();
    }

    public BookService(BookDao bookDao) {
        this.bookDao = bookDao;
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

    public void createNewBook(Book book){
        bookDao.create(book);
    }

    public List<Book> findByAuthor(Author author){
        return bookDao.findByAuthorId(author.getId());
    }

    public void updateBook(Book book){
        bookDao.update(book);
    }

    public void deleteBook(Book book){
        bookDao.delete(book);
    }
}

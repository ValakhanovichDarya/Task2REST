package com.services;

import com.dao.AuthorDao;
import com.model.entity.Author;

import java.util.List;

public class AuthorService {
    private static AuthorService INSTANCE = null;
    private AuthorDao authorDao;
    private BookService bookService;

    private AuthorService() {
        authorDao = AuthorDao.getInstance();
        bookService = BookService.getInstance();
    }

    public AuthorService(AuthorDao authorDao, BookService bookService) {
        this.authorDao = authorDao;
        this.bookService = bookService;
    }

    public static AuthorService getInstance() {
        if (INSTANCE == null) {
            synchronized (AuthorService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AuthorService();
                }
            }
        }
        return INSTANCE;
    }

    public void createNewAuthor(Author author){
        authorDao.create(author);
    }

    public List<Author> findAll(){
        List<Author> authors = authorDao.findAll();
        for(Author author: authors)
            author.setBooks(bookService.findByAuthor(author));
        return authors;
    }
}

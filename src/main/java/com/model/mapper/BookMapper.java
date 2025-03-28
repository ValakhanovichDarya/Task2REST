package com.model.mapper;

import com.model.dto.BookDto;
import com.model.dto.ResponseBookDto;
import com.model.entity.Book;

public class BookMapper {
    private static BookMapper  INSTANCE = null;
    private BookMapper () {
    }

    public static BookMapper  getInstance() {
        if (INSTANCE == null) {
            synchronized (BookMapper .class) {
                if (INSTANCE == null) {
                    INSTANCE = new BookMapper ();
                }
            }
        }
        return INSTANCE;
    }

    public Book toBook(BookDto bookDto) {
        return new Book(bookDto.getId(), bookDto.getName(), bookDto.getNumberOfPages(), AuthorMapper.getInstance().toAuthor(bookDto.getAuthorDto()));
    }

    public BookDto toBookDto(Book book) {
        return new BookDto(book.getId(), book.getName(), book.getNumberOfPages(), AuthorMapper.getInstance().toAuthorDto(book.getAuthor()));
    }

    public ResponseBookDto toResponseBookDto(Book book) {
        return new ResponseBookDto(book.getId(), book.getName(), book.getNumberOfPages());
    }

}

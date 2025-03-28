package com.model.mapper;

import com.model.dto.AuthorDto;
import com.model.dto.ResponseAuthorDto;
import com.model.dto.ResponseBookDto;
import com.model.entity.Author;
import com.model.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class AuthorMapper {
    private static AuthorMapper  INSTANCE = null;
    private AuthorMapper () {
    }

    public static AuthorMapper  getInstance() {
        if (INSTANCE == null) {
            synchronized (AuthorMapper .class) {
                if (INSTANCE == null) {
                    INSTANCE = new AuthorMapper ();
                }
            }
        }
        return INSTANCE;
    }

    public Author toAuthor(AuthorDto authorDto) {
        return new Author(authorDto.getId(), authorDto.getName());
    }

    public AuthorDto toAuthorDto(Author author) {
        return new AuthorDto(author.getId(), author.getName());
    }

    public ResponseAuthorDto toResponseAuthorDto(Author author) {
        List<ResponseBookDto> bookDtos = new ArrayList<>();
        List<Book> books = author.getBooks();
        for(Book book : books)
            bookDtos.add(BookMapper.getInstance().toResponseBookDto(book));
        return new ResponseAuthorDto(author.getId(), author.getName(), bookDtos);
    }
}

package com.mapper;

import com.model.dto.AuthorDto;
import com.model.dto.BookDto;
import com.model.dto.ResponseBookDto;
import com.model.entity.Author;
import com.model.entity.Book;
import com.model.mapper.BookMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookMapperTest {
    private final BookMapper bookMapper = BookMapper.getInstance();

    @Test
    void testToBook() {
        AuthorDto authorDto = new AuthorDto(1L, "Test Author");
        BookDto bookDto = new BookDto(1L, "Test Book", 100, authorDto);

        Book book = bookMapper.toBook(bookDto);

        assertNotNull(book);
        assertEquals(bookDto.getId(), book.getId());
        assertEquals(bookDto.getName(), book.getName());
        assertEquals(bookDto.getNumberOfPages(), book.getNumberOfPages());
        assertNotNull(book.getAuthor());
        assertEquals(authorDto.getId(), book.getAuthor().getId());
        assertEquals(authorDto.getName(), book.getAuthor().getName());
    }

    @Test
    void testToBookDto() {
        Author author = new Author(1L, "Test Author");
        Book book = new Book(1L, "Test Book", 100, author);

        BookDto bookDto = bookMapper.toBookDto(book);

        assertNotNull(bookDto);
        assertEquals(book.getId(), bookDto.getId());
        assertEquals(book.getName(), bookDto.getName());
        assertEquals(book.getNumberOfPages(), bookDto.getNumberOfPages());
        assertNotNull(bookDto.getAuthorDto());
        assertEquals(author.getId(), bookDto.getAuthorDto().getId());
        assertEquals(author.getName(), bookDto.getAuthorDto().getName());
    }

    @Test
    void testToResponseBookDto() {
        Author author = new Author(1L, "Test Author");
        Book book = new Book(1L, "Test Book", 100, author);

        ResponseBookDto responseBookDto = bookMapper.toResponseBookDto(book);

        assertNotNull(responseBookDto);
        assertEquals(book.getId(), responseBookDto.getId());
        assertEquals(book.getName(), responseBookDto.getName());
        assertEquals(book.getNumberOfPages(), responseBookDto.getNumberOfPages());
    }
}


package com.mapper;

import com.model.dto.AuthorDto;
import com.model.dto.ResponseAuthorDto;
import com.model.dto.ResponseBookDto;
import com.model.entity.Author;
import com.model.entity.Book;
import com.model.mapper.AuthorMapper;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AuthorMapperTest {
    private final AuthorMapper authorMapper = AuthorMapper.getInstance();

    @Test
    void testToAuthor() {
        AuthorDto authorDto = new AuthorDto(1L, "Test Author");

        Author author = authorMapper.toAuthor(authorDto);

        assertNotNull(author);
        assertEquals(authorDto.getId(), author.getId());
        assertEquals(authorDto.getName(), author.getName());
    }

    @Test
    void testToAuthorDto() {
        Author author = new Author(1L, "Test Author");

        AuthorDto authorDto = authorMapper.toAuthorDto(author);

        assertNotNull(authorDto);
        assertEquals(author.getId(), authorDto.getId());
        assertEquals(author.getName(), authorDto.getName());
    }

    @Test
    void testToResponseAuthorDto() {
        Author author = new Author(1L, "Test Author");
        Book book1 = new Book(1L, "Book One", 100, author);
        Book book2 = new Book(2L, "Book Two", 200, author);
        List<Book> books = Arrays.asList(book1, book2);
        author.setBooks(books);

        ResponseAuthorDto responseAuthorDto = authorMapper.toResponseAuthorDto(author);

        assertNotNull(responseAuthorDto);
        assertEquals(author.getId(), responseAuthorDto.getId());
        assertEquals(author.getName(), responseAuthorDto.getName());
        assertEquals(books.size(), responseAuthorDto.getBooks().size());
        List<ResponseBookDto> responseBooks = responseAuthorDto.getBooks();
        assertEquals("Book One", responseBooks.get(0).getName());
        assertEquals("Book Two", responseBooks.get(1).getName());
    }
}


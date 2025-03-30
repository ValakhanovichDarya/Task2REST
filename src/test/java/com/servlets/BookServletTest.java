package com.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.AuthorDto;
import com.model.dto.BookDto;
import com.model.entity.Author;
import com.model.entity.Book;
import com.model.mapper.AuthorMapper;
import com.model.mapper.BookMapper;
import com.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class BookServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private BookService bookService;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private BookServlet bookServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doGetToGetBooksByAuthor() throws IOException, SQLException {

        String json = "{\"id\":\"1\", \"name\":\"Author One\"}";
        AuthorDto authorDto = new AuthorDto(1, "Author One");

        Book book1 = new Book(1, "Book One", 100, new Author(1, "Author One"));
        Book book2 = new Book(2, "Book Two", 150, new Author(1, "Author One"));

        List<Book> books = Arrays.asList(book1, book2);
        List<BookDto> bookDtos = Arrays.asList(new BookDto(1, "Book One", 100, authorDto), new BookDto(2, "Book Two", 150, authorDto));

        try (MockedStatic<ServletsUtil> mockedServletsUtil = mockStatic(ServletsUtil.class);
             MockedStatic<BookService> mockedBookService = mockStatic(BookService.class);
             MockedStatic<AuthorMapper> mockedAuthorMapper = mockStatic(AuthorMapper.class);
             MockedStatic<BookMapper> mockedBookMapper = mockStatic(BookMapper.class)) {

            mockedBookService.when(BookService::getInstance).thenReturn(bookService);
            mockedAuthorMapper.when(AuthorMapper::getInstance).thenReturn(authorMapper);
            mockedBookMapper.when(BookMapper::getInstance).thenReturn(bookMapper);

            mockedServletsUtil.when(() -> ServletsUtil.readJson(request)).thenReturn(json);
            when(bookService.findByAuthor(any())).thenReturn(books);
            when(authorMapper.toAuthor(authorDto)).thenReturn(new com.model.entity.Author(1, "Author One"));
            when(bookMapper.toBookDto(book1)).thenReturn(new BookDto(1, "Book One", 100, authorDto));
            when(bookMapper.toBookDto(book2)).thenReturn(new BookDto(2, "Book Two", 150, authorDto));

            doNothing().when(ServletsUtil.class);
            ServletsUtil.writeJson(response, bookDtos);
            mockedServletsUtil.when(() -> ServletsUtil.readJson(request)).thenReturn(json);

            bookServlet.doGet(request, response);

            verify(bookService, times(1)).findByAuthor(any());
            verify(authorMapper, times(1)).toAuthor(authorDto);
            verify(bookMapper, times(1)).toBookDto(book1);
            verify(bookMapper, times(1)).toBookDto(book2);

            mockedServletsUtil.verify(() -> ServletsUtil.writeJson(response, bookDtos), times(1));
            mockedServletsUtil.verify(() -> ServletsUtil.writeJson(response, bookDtos), times(1));
        }
    }

    @Test
    void doPutToUpdateBook() throws IOException, SQLException {
        BookDto bookDto = new BookDto(1, "Book One", 200, new AuthorDto(1, "Author One"));
        String json = new ObjectMapper().writeValueAsString(bookDto);

        BufferedReader reader = new BufferedReader(new StringReader(json));
        when(request.getReader()).thenReturn(reader);

        try (MockedStatic<ServletsUtil> mockedServletsUtil = mockStatic(ServletsUtil.class);
             MockedStatic<BookService> mockedBookService = mockStatic(BookService.class);
             MockedStatic<BookMapper> mockedBookMapper = mockStatic(BookMapper.class)) {

            BookService bookService = mock(BookService.class);
            mockedBookService.when(BookService::getInstance).thenReturn(bookService);
            mockedServletsUtil.when(() -> ServletsUtil.readJson(request)).thenReturn(json);

            mockedBookMapper.when(BookMapper::getInstance).thenReturn(bookMapper);
            when(bookMapper.toBook(any(BookDto.class))).thenReturn(new Book());

            doNothing().when(bookService).updateBook(any());

            bookServlet.doPut(request, response);

            verify(bookService, times(1)).updateBook(any());
            verify(response, times(0)).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Test
    void doPostToCreateNewBook() throws SQLException {

        String json = "{\"name\":\"Test Book\",\"numberOfPages\":200,\"authorDto\":{\"name\":\"Test Author\"}}";

        try (MockedStatic<BookMapper> mockedBookMapper = mockStatic(BookMapper.class);
             MockedStatic<BookService> mockedBookService = mockStatic(BookService.class);
             MockedStatic<ServletsUtil> mockedServletsUtil = mockStatic(ServletsUtil.class)) {

            mockedBookMapper.when(BookMapper::getInstance).thenReturn(bookMapper);
            mockedBookService.when(BookService::getInstance).thenReturn(bookService);
            mockedServletsUtil.when(() -> ServletsUtil.readJson(request)).thenReturn(json);

            when(bookMapper.toBook(any(BookDto.class))).thenReturn(new Book());
            doNothing().when(bookService).createNewBook(any());

            bookServlet.doPost(request, response);

            verify(bookService, times(1)).createNewBook(any());
        }
    }

    @Test
    void doDeleteTodDeleteBook() throws SQLException{

        String json = "{\"name\":\"Test Book\",\"numberOfPages\":200,\"authorDto\":{\"name\":\"Test Author\"}}";

        try (MockedStatic<BookMapper> mockedBookMapper = mockStatic(BookMapper.class);
             MockedStatic<BookService> mockedBookService = mockStatic(BookService.class);
             MockedStatic<ServletsUtil> mockedServletsUtil = mockStatic(ServletsUtil.class)) {

            mockedBookMapper.when(BookMapper::getInstance).thenReturn(bookMapper);
            mockedBookService.when(BookService::getInstance).thenReturn(bookService);
            mockedServletsUtil.when(() -> ServletsUtil.readJson(request)).thenReturn(json);

            when(bookMapper.toBook(any(BookDto.class))).thenReturn(new Book());
            doNothing().when(bookService).deleteBook(any());

            bookServlet.doDelete(request, response);

            verify(bookService, times(1)).deleteBook(any());
        }
    }

}
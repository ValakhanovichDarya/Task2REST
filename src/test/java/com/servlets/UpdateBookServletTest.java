package com.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.AuthorDto;
import com.model.dto.BookDto;
import com.model.entity.Book;
import com.model.mapper.BookMapper;
import com.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.StringReader;

import static org.mockito.Mockito.*;

class UpdateBookServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private UpdateBookServlet updateBookServlet;

    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookDto = new BookDto(1, "Book One", 200, new AuthorDto(1, "Author One"));
    }

    @Test
    void doPutToUpdateBook() throws Exception {
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

            updateBookServlet.doPut(request, response);

            verify(bookService, times(1)).updateBook(any());
            verify(response, times(0)).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}


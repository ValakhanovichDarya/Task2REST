package com.servlets;

import com.model.dto.BookDto;
import com.model.entity.Book;
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

import static org.mockito.Mockito.*;

class AddBookServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private BookService bookService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private AddBookServlet addBookServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doPostToCreateNewBook() {

        String json = "{\"name\":\"Test Book\",\"numberOfPages\":200,\"authorDto\":{\"name\":\"Test Author\"}}";

        try (MockedStatic<BookMapper> mockedBookMapper = mockStatic(BookMapper.class);
             MockedStatic<BookService> mockedBookService = mockStatic(BookService.class);
             MockedStatic<ServletsUtil> mockedServletsUtil = mockStatic(ServletsUtil.class)) {

            mockedBookMapper.when(BookMapper::getInstance).thenReturn(bookMapper);
            mockedBookService.when(BookService::getInstance).thenReturn(bookService);
            mockedServletsUtil.when(() -> ServletsUtil.readJson(request)).thenReturn(json);

            when(bookMapper.toBook(any(BookDto.class))).thenReturn(new Book());
            doNothing().when(bookService).createNewBook(any());

            addBookServlet.doPost(request, response);

            verify(bookService, times(1)).createNewBook(any());
        }
    }

}
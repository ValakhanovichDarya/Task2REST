package com.servlets;

import com.model.dto.AuthorDto;
import com.model.dto.ResponseAuthorDto;
import com.model.dto.ResponseBookDto;
import com.model.entity.Author;
import com.model.mapper.AuthorMapper;
import com.services.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class AuthorServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthorService authorService;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorServlet authorServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doGetToGetAllAuthors() throws IOException, SQLException {

        Author author1 = new Author(1, "Author One");
        Author author2 = new Author(2, "Author Two");

        ResponseBookDto book1 = new ResponseBookDto(1, "Book One", 100);
        ResponseBookDto book2 = new ResponseBookDto(2, "Book Two", 200);

        List<ResponseBookDto> books1 = Collections.singletonList(book1);
        List<ResponseBookDto> books2 = Collections.singletonList(book2);

        ResponseAuthorDto responseAuthorDto1 = new ResponseAuthorDto(1, "Author One", books1);
        ResponseAuthorDto responseAuthorDto2 = new ResponseAuthorDto(2, "Author Two", books2);

        List<Author> authors = Arrays.asList(author1, author2);
        List<ResponseAuthorDto> dtos = Arrays.asList(responseAuthorDto1, responseAuthorDto2);

        try (MockedStatic<AuthorService> mockedAuthorService = mockStatic(AuthorService.class);
             MockedStatic<AuthorMapper> mockedAuthorMapper = mockStatic(AuthorMapper.class);
             MockedStatic<ServletsUtil> mockedServletsUtil = mockStatic(ServletsUtil.class)) {

            mockedAuthorService.when(AuthorService::getInstance).thenReturn(authorService);
            mockedAuthorMapper.when(AuthorMapper::getInstance).thenReturn(authorMapper);

            doNothing().when(ServletsUtil.class);
            ServletsUtil.writeJson(response, dtos);

            when(authorService.findAll()).thenReturn(authors);
            when(authorMapper.toResponseAuthorDto(author1)).thenReturn(responseAuthorDto1);
            when(authorMapper.toResponseAuthorDto(author2)).thenReturn(responseAuthorDto2);

            authorServlet.doGet(request, response);

            verify(authorService, times(1)).findAll();
            verify(authorMapper, times(1)).toResponseAuthorDto(author1);
            verify(authorMapper, times(1)).toResponseAuthorDto(author2);
            mockedServletsUtil.verify(() -> ServletsUtil.writeJson(response, dtos), times(1));
        }
    }

    @Test
    void doPostToCreateNewAuthor() throws SQLException{

        String json = "{\"name\":\"Test Author\"}";

        try (MockedStatic<AuthorMapper> mockedAuthorMapper = mockStatic(AuthorMapper.class);
             MockedStatic<AuthorService> mockedAuthorService = mockStatic(AuthorService.class);
             MockedStatic<ServletsUtil> mockedServletsUtil = mockStatic(ServletsUtil.class)) {

            mockedAuthorMapper.when(AuthorMapper::getInstance).thenReturn(authorMapper);
            mockedAuthorService.when(AuthorService::getInstance).thenReturn(authorService);
            mockedServletsUtil.when(() -> ServletsUtil.readJson(request)).thenReturn(json);

            when(authorMapper.toAuthor(any(AuthorDto.class))).thenReturn(new Author());
            doNothing().when(authorService).createNewAuthor(any());

            authorServlet.doPost(request, response);

            verify(authorService, times(1)).createNewAuthor(any());
        }
    }
}

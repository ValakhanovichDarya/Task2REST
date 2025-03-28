package com.servlets;

import com.model.dto.AuthorDto;
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

import static org.mockito.Mockito.*;

class CreateAuthorServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthorService authorService;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private CreateAuthorServlet createAuthorServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doPostToCreateNewAuthor() {

        String json = "{\"name\":\"Test Author\"}";

        try (MockedStatic<AuthorMapper> mockedAuthorMapper = mockStatic(AuthorMapper.class);
             MockedStatic<AuthorService> mockedAuthorService = mockStatic(AuthorService.class);
             MockedStatic<ServletsUtil> mockedServletsUtil = mockStatic(ServletsUtil.class)) {

            mockedAuthorMapper.when(AuthorMapper::getInstance).thenReturn(authorMapper);
            mockedAuthorService.when(AuthorService::getInstance).thenReturn(authorService);
            mockedServletsUtil.when(() -> ServletsUtil.readJson(request)).thenReturn(json);

            when(authorMapper.toAuthor(any(AuthorDto.class))).thenReturn(new Author());
            doNothing().when(authorService).createNewAuthor(any());

            createAuthorServlet.doPost(request, response);

            verify(authorService, times(1)).createNewAuthor(any());
        }
    }
}
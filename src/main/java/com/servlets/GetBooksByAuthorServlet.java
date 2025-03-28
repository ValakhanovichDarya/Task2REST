package com.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.AuthorDto;
import com.model.dto.BookDto;
import com.model.entity.Book;
import com.model.mapper.AuthorMapper;
import com.model.mapper.BookMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GetBooksByAuthorServlet", urlPatterns = "/getBooksByAuthor")
public class GetBooksByAuthorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            AuthorDto authorDto = new ObjectMapper().readValue(ServletsUtil.readJson(request), AuthorDto.class);
            List<Book> books = BookService.getInstance().findByAuthor(AuthorMapper.getInstance().toAuthor(authorDto));
            List<BookDto> bookDtos = new ArrayList<>();
            for(Book book: books){
                bookDtos.add(BookMapper.getInstance().toBookDto(book));
            }
            ServletsUtil.writeJson(response, bookDtos);
        } catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
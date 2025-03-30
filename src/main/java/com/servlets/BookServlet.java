package com.servlets;

import com.model.dto.AuthorDto;
import com.model.entity.Book;
import com.model.mapper.AuthorMapper;
import com.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.BookDto;
import com.model.mapper.BookMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "AddBookServlet", urlPatterns = "/book")
public class BookServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(AuthorServlet.class);

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
        } catch (SQLException | IOException e){
            logger.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {
            BookDto bookDto = new ObjectMapper().readValue(ServletsUtil.readJson(request), BookDto.class);
            BookService.getInstance().createNewBook(BookMapper.getInstance().toBook(bookDto));
        } catch (SQLException | IOException e){
            logger.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {

        try {
            BookDto bookDto = new ObjectMapper().readValue(ServletsUtil.readJson(request), BookDto.class);
            BookService.getInstance().updateBook(BookMapper.getInstance().toBook(bookDto));
        } catch (SQLException | IOException e){
            logger.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {

        try {
            BookDto bookDto = new ObjectMapper().readValue(ServletsUtil.readJson(request), BookDto.class);
            BookService.getInstance().deleteBook(BookMapper.getInstance().toBook(bookDto));
        } catch (SQLException | IOException e){
            logger.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

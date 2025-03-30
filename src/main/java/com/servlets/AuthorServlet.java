package com.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.AuthorDto;
import com.services.AuthorService;
import com.model.dto.ResponseAuthorDto;
import com.model.entity.Author;
import com.model.mapper.AuthorMapper;
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


@WebServlet(name = "GetAllAuthorsServlet", urlPatterns = "/author/*")
public class AuthorServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(AuthorServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            List<Author> authors = AuthorService.getInstance().findAll();
            List<ResponseAuthorDto> dtos = new ArrayList<>();
            for(Author author : authors)
                dtos.add(AuthorMapper.getInstance().toResponseAuthorDto(author));
            ServletsUtil.writeJson(response, dtos);
        } catch (SQLException | IOException e){
            logger.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {
            AuthorDto authorDto = new ObjectMapper().readValue(ServletsUtil.readJson(request), AuthorDto.class);
            AuthorService.getInstance().createNewAuthor(AuthorMapper.getInstance().toAuthor(authorDto));
        } catch (SQLException | IOException e){
            logger.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

}

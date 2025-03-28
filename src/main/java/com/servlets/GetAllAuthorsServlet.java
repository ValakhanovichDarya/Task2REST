package com.servlets;

import com.services.AuthorService;
import com.model.dto.ResponseAuthorDto;
import com.model.entity.Author;
import com.model.mapper.AuthorMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "GetAllAuthorsServlet", urlPatterns = "/getAuthors")
public class GetAllAuthorsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            List<Author> authors = AuthorService.getInstance().findAll();
            List<ResponseAuthorDto> dtos = new ArrayList<>();
            for(Author author : authors){
                dtos.add(AuthorMapper.getInstance().toResponseAuthorDto(author));
            }
            ServletsUtil.writeJson(response, dtos);
        } catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}

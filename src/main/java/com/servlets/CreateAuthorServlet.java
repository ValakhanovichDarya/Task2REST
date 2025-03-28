package com.servlets;

import com.services.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.AuthorDto;
import com.model.mapper.AuthorMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "CreateAuthorServlet", urlPatterns = "/createAuthor")
public class CreateAuthorServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {
            AuthorDto authorDto = new ObjectMapper().readValue(ServletsUtil.readJson(request), AuthorDto.class);
            AuthorService.getInstance().createNewAuthor(AuthorMapper.getInstance().toAuthor(authorDto));
        } catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}

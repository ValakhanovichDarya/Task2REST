package com.servlets;

import com.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.BookDto;
import com.model.mapper.BookMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "UpdateBookServlet", urlPatterns = "/updateBook")
public class UpdateBookServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {

        try {
            BookDto bookDto = new ObjectMapper().readValue(ServletsUtil.readJson(request), BookDto.class);
            BookService.getInstance().updateBook(BookMapper.getInstance().toBook(bookDto));
        } catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
package com.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

class ServletsUtil {
    static String readJson(HttpServletRequest request) throws IOException {
        Scanner scanner = new Scanner(request.getInputStream(), StandardCharsets.UTF_8);
        String jsonData = scanner.useDelimiter("\\A").next();
        scanner.close();
        return jsonData;
    }

    static void writeJson(HttpServletResponse response, Object dto) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(dto);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(json);
        out.flush();
    }

}

package com.thesnoozingturtle.moneymanagerrestapi.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thesnoozingturtle.moneymanagerrestapi.payload.ApiResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ApiResponse apiResponse = new ApiResponse(accessDeniedException.getMessage(), false);
        response.setContentType("application/json");
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(outputStream, apiResponse);
        outputStream.flush();
    }
}

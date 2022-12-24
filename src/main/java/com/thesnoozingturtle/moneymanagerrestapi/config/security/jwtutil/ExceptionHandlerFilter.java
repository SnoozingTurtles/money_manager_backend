package com.thesnoozingturtle.moneymanagerrestapi.config.security.jwtutil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thesnoozingturtle.moneymanagerrestapi.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException exception) {
            ApiResponse apiResponse = new ApiResponse(exception.getMessage(), false);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write(convertObjectToJson(apiResponse));
        }
    }

    private String convertObjectToJson(ApiResponse apiResponse) throws JsonProcessingException {
        if (apiResponse == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(apiResponse);
    }
}

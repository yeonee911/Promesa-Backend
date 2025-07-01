package com.promesa.promesa.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.promesa.promesa.common.dto.ErrorResponse;
import com.promesa.promesa.security.jwt.exception.ExpiredJwtException;
import com.promesa.promesa.security.jwt.exception.InvalidJwtException;
import com.promesa.promesa.security.jwt.exception.JwtErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        JwtErrorCode errorCode;

        if (authException instanceof InvalidJwtException) {
            errorCode = ((InvalidJwtException) authException).getErrorCode();
        } else if (authException instanceof ExpiredJwtException) {
            errorCode = ((ExpiredJwtException) authException).getErrorCode();
        } else {
            errorCode = JwtErrorCode.INVALID_TOKEN;
        }

        ErrorResponse errorResponse = ErrorResponse.from(errorCode.getErrorReason(), request.getRequestURI());

        response.setStatus(errorResponse.getStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

}

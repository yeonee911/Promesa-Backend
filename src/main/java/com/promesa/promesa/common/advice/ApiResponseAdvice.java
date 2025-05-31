package com.promesa.promesa.common.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.promesa.promesa.common.dto.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "com.promesa.promesa")
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper; // JacksonConfig의 Bean 주입

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,  // Controller 메서드에서 반환한 응답 데이터
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        HttpServletResponse servletResponse =
                ((ServletServerHttpResponse) response).getServletResponse();

        int status = servletResponse.getStatus();
        HttpStatus resolve = HttpStatus.resolve(status);    // HTTP status 코드를 HttpStatus enum으로 변환

        if (resolve == null) {
            return body;
        }

        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(SuccessResponse.success(status, body));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON 변환 실패", e);
            }
        }

        if (resolve.is2xxSuccessful()) {    // 성공 응답일 경우
            return SuccessResponse.success(status, body);
        }

        return body;
    }
}
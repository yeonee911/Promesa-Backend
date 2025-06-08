package com.promesa.promesa.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://ceos-promesa.vercel.app",
                        "http://13.209.202.120:8081",
                        "http://localhost:8080") // 허용할 출처
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true); // 쿠키 인증 요청 허용
    }
}
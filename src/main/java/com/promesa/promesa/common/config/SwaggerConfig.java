package com.promesa.promesa.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "프로메사 API 명세서",
                description = "도예 전공자들의 작품을 전시하고 판매하는 플랫폼, 프로메사(Promesa)"
        )
)

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI();
    }
}
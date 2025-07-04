package com.promesa.promesa.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promesa.promesa.common.application.OAuth2Service;
import com.promesa.promesa.security.jwt.CustomUserDetailsService;
import com.promesa.promesa.security.jwt.JwtAccessDeniedHandler;
import com.promesa.promesa.security.jwt.JwtAuthenticationEntryPoint;
import com.promesa.promesa.security.jwt.JwtAuthenticationFilter;
import com.promesa.promesa.security.jwt.JwtUtil;
import com.promesa.promesa.security.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2Service oAuth2Service;
    private final JwtUtil jwtUtil;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomUserDetailsService userDetailsService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/reviews/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/review-images/**").permitAll()
                        .requestMatchers(
                                "/actuator/**",
                                "/", "/login", "/signup",
                                "/brand-info", "/categories/**", "exhibitions/**","/inquiries/**","/artists/**",
                                "/index.html", "/static/**", "/favicon.ico",
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/h2-console/**"
                        ).permitAll()                        // 위 경로는 인증 없이 접근 허용
                        .requestMatchers("/oauth/loginInfo").authenticated() // 이건 명시적으로 인증 필요
                        .anyRequest().authenticated()       // 나머지는 모두 인증 필요
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2Service))
                        .successHandler(oAuth2SuccessHandler)
                );

        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailsService,objectMapper), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
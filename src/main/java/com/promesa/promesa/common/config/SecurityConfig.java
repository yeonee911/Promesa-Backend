package com.promesa.promesa.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promesa.promesa.common.application.OAuth2Service;
import com.promesa.promesa.security.jwt.CustomUserDetailsService;
import com.promesa.promesa.security.jwt.JwtAccessDeniedHandler;
import com.promesa.promesa.security.jwt.JwtAuthenticationEntryPoint;
import com.promesa.promesa.security.jwt.JwtAuthenticationFilter;
import com.promesa.promesa.security.jwt.JwtUtil;
import com.promesa.promesa.security.oauth.OAuth2SuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
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
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            ClientRegistrationRepository clientRegistrationRepository
    ) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/items/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/review-images/**").permitAll()
                        .requestMatchers(
                                "/actuator/**",
                                "/", "/login", "/signup",
                                "/brand-info", "/categories/**", "/exhibitions/**", "/inquiries/**", "/artists/**",
                                "/index.html", "/static/**", "/favicon.ico",
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/h2-console/**","dev/**"
                        ).permitAll()                    
                        .requestMatchers("/oauth/loginInfo").authenticated()
                        .anyRequest().authenticated()

                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestResolver(
                                        customAuthorizationRequestResolver(clientRegistrationRepository)
                                )
                        )
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2Service))
                        .successHandler(oAuth2SuccessHandler)
                );

        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailsService, objectMapper), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver(
            ClientRegistrationRepository repo
    ) {
        DefaultOAuth2AuthorizationRequestResolver defaultResolver =
                new DefaultOAuth2AuthorizationRequestResolver(repo, "/oauth2/authorization");

        return new OAuth2AuthorizationRequestResolver() {
            @Override
            public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
                OAuth2AuthorizationRequest originalRequest = defaultResolver.resolve(request);
                if (originalRequest == null) return null;

                String stateParam = request.getParameter("state");
                OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.from(originalRequest);

                if (stateParam != null && !stateParam.isBlank()) {
                    builder.state(stateParam);
                }

                return builder.build();
            }

            @Override
            public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
                OAuth2AuthorizationRequest originalRequest = defaultResolver.resolve(request, clientRegistrationId);
                if (originalRequest == null) return null;

                String stateParam = request.getParameter("state");
                OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.from(originalRequest);

                if (stateParam != null && !stateParam.isBlank()) {
                    builder.state(stateParam);
                }

                return builder.build();
            }
        };
    }
}

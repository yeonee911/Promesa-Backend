package com.promesa.promesa.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promesa.promesa.common.dto.ErrorResponse;
import com.promesa.promesa.security.jwt.exception.JwtErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // 토큰이 없을 경우 처리
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // 일단 인증 없이 접근 허용 -> 로그인 필수인 API에서 나중에 처리
            return;
        }

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                jwtUtil.isExpired(token);

                String username = jwtUtil.getNickname(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (com.promesa.promesa.security.jwt.exception.ExpiredJwtException e) {
                sendErrorResponse(response, JwtErrorCode.EXPIRED_TOKEN, request.getRequestURI());
                return;
            } catch (com.promesa.promesa.security.jwt.exception.InvalidJwtException e) {
                sendErrorResponse(response, JwtErrorCode.INVALID_TOKEN, request.getRequestURI());
                return;
            } catch (io.jsonwebtoken.MalformedJwtException |
                     io.jsonwebtoken.UnsupportedJwtException |
                     IllegalArgumentException e) {
                sendErrorResponse(response, JwtErrorCode.INVALID_TOKEN_FORMAT, request.getRequestURI());
                return;
            } catch (io.jsonwebtoken.JwtException e) {
                sendErrorResponse(response, JwtErrorCode.INVALID_TOKEN, request.getRequestURI());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, JwtErrorCode errorCode, String path) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.from(errorCode.getErrorReason(), path);

        response.setStatus(errorResponse.getStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.flushBuffer();
    }
}
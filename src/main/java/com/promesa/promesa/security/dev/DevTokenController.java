package com.promesa.promesa.security.dev;

import com.promesa.promesa.security.jwt.JwtTokenProvider;
import com.promesa.promesa.security.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dev")
public class DevTokenController {

    private final JwtUtil jwtUtil;

    @PostMapping("/test-token")
    @Operation(summary = "테스트용 액세스 토큰 발급 (개발 전용)")
    public String generateTestToken(@RequestParam(defaultValue = "kakao:1234") String nickname) {
        List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN");
        return jwtUtil.createAccessToken(nickname, roles); // 운영 토큰처럼 claim 다 넣기
    }
}

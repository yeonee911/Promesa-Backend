package com.promesa.promesa.security.dev;

import com.promesa.promesa.security.jwt.JwtTokenProvider;
import com.promesa.promesa.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dev")
public class DevTokenController {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;

    @PostMapping("/test-token")
    public String generateTestToken(@RequestParam(defaultValue = "kakao:1234") String nickname) {
        return jwtUtil.createAccessToken(nickname, "USER"); // 운영 토큰처럼 claim 다 넣기
    }


}

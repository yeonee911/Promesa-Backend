package com.promesa.promesa.security.dev;

import com.promesa.promesa.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dev")
public class DevTokenController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/test-token")
    public String generateTestToken() {
        return jwtTokenProvider.generateAccessToken("kakao:1234");
    }

}

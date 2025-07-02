package com.promesa.promesa.security.jwt.refresh;

import com.promesa.promesa.common.dto.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class RefreshController {

    private final RefreshService refreshService;

    @PostMapping("/reissue")
    public ResponseEntity<SuccessResponse<?>> reissue(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(refreshService.reissue(request, response));
    }
}
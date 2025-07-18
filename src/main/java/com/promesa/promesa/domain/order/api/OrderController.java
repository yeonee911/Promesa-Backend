package com.promesa.promesa.domain.order.api;

import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.order.application.OrderService;
import com.promesa.promesa.domain.order.dto.OrderRequest;
import com.promesa.promesa.domain.order.dto.OrderResponse;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "주문 생성")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        Member member = user.getMember();
        return ResponseEntity.ok(orderService.createOrder(request, member));
    }
}

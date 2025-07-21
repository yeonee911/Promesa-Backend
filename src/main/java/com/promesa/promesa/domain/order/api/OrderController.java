package com.promesa.promesa.domain.order.api;

import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.order.application.OrderService;
import com.promesa.promesa.domain.order.dto.OrderDetail;
import com.promesa.promesa.domain.order.dto.OrderRequest;
import com.promesa.promesa.domain.order.dto.OrderResponse;
import com.promesa.promesa.domain.order.dto.OrderSummary;
import com.promesa.promesa.domain.shippingAddress.application.ShippingAddressService;
import com.promesa.promesa.domain.shippingAddress.dto.request.AddressRequest;
import com.promesa.promesa.domain.shippingAddress.dto.response.AddressResponse;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final ShippingAddressService shippingAddressService;
    private final OrderService orderService;

    @PostMapping("/address")
    @Operation(summary = "기본 배송지 정보를 추가 또는 수정")
    public ResponseEntity<AddressResponse> addOrUpdateShippingAddress(
            @RequestBody @Valid AddressRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Member member = (user != null) ? user.getMember() : null;
        AddressResponse response = shippingAddressService.addOrUpdateShippingAddress(request, member);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "주문 생성")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody @Valid OrderRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        Member member = user.getMember();
        return ResponseEntity.ok(orderService.createOrder(request, member));
    }

    @GetMapping
    @Operation(summary = "주문 내역 목록 조회")
    public ResponseEntity<List<OrderSummary>> getOrders(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Member member = user.getMember();
        return ResponseEntity.ok(orderService.getOrderSummaries(member));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "주문 상세 내역 조회")
    public ResponseEntity<OrderDetail> getOrderDetail(
            @PathVariable Long orderId,
            @AuthenticationPrincipal CustomUserDetails user) {
        Member member = user.getMember();
        return ResponseEntity.ok(orderService.getOrderDetail(member, orderId));
    }
}

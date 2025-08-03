package com.promesa.promesa.domain.order.api;

import com.promesa.promesa.domain.order.application.OrderService;
import com.promesa.promesa.domain.order.domain.OrderItemStatus;
import com.promesa.promesa.domain.order.domain.OrderStatus;
import com.promesa.promesa.domain.order.dto.request.OrderItemStatusRequest;
import com.promesa.promesa.domain.order.dto.request.OrderStatusRequest;
import com.promesa.promesa.domain.order.dto.response.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final OrderService orderService;

    @Operation(summary = "주문과 주문 아이템 목록 조회")
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getOrders(
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestParam(required = false) OrderItemStatus itemStatus
    ) {
        return ResponseEntity.ok(orderService.getOrders(orderStatus, itemStatus));
    }

    @Operation(summary = "주문 상태 변경")
    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusRequest request
    ) {
        orderService.updateOrderStatus(orderId, request.orderStatus());
        String message = "주문 상태가 변경되었습니다.";
        return ResponseEntity.ok(message);
    }

    @Operation(summary = "주문 아이템 상태 변경")
    @PatchMapping("/order-items/{orderItemId}")
    public ResponseEntity<String> updateOrderItemStatus(
            @PathVariable Long orderItemId,
            @RequestBody OrderItemStatusRequest request
    ) {
        orderService.updateOrderItemStatus(orderItemId, request.orderItemStatus());
        String message = "주문 아이템 상태가 변경되었습니다.";
        return ResponseEntity.ok(message);
    }
}

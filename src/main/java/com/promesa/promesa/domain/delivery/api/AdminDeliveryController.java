package com.promesa.promesa.domain.delivery.api;

import com.promesa.promesa.domain.delivery.application.DeliveryService;
import com.promesa.promesa.domain.delivery.dto.request.DeliveryRequest;
import com.promesa.promesa.domain.delivery.dto.response.DeliveryResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "배송 내역 조회")
    @GetMapping("/orders/{orderId}/deliveries")
    public ResponseEntity<DeliveryResponse> getDelivery(@PathVariable Long orderId) {
        DeliveryResponse response = deliveryService.getDelivery(orderId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "배송 내역 수정")
    @PutMapping("/deliveries/{deliveryId}")
    public ResponseEntity<DeliveryResponse> updateDelivery(
            @PathVariable Long deliveryId,
            @RequestBody DeliveryRequest request
    ) {
        DeliveryResponse response = deliveryService.updateDelivery(deliveryId, request);
        return ResponseEntity.ok(response);
    }
}

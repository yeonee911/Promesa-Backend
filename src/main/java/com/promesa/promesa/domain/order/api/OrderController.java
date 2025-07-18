package com.promesa.promesa.domain.order.api;

import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.shippingAddress.application.ShippingAddressService;
import com.promesa.promesa.domain.shippingAddress.dto.request.AddressRequest;
import com.promesa.promesa.domain.shippingAddress.dto.response.AddressResponse;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
    private final ShippingAddressService shippingAddressService;

    @PostMapping("/address")
    @Operation(summary = "기본 배송지 정보를 추가 또는 수정")
    public ResponseEntity<AddressResponse> addOrUpdateShippingAddress(
            @RequestBody @Valid AddressRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    )
    {
        Member member = (user != null) ? user.getMember() : null;
        AddressResponse response = shippingAddressService.addOrUpdateShippingAddress(request, member);
        return ResponseEntity.ok(response);
    }
}

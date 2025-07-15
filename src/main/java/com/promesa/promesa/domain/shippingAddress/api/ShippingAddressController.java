package com.promesa.promesa.domain.shippingAddress.api;

import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.shippingAddress.application.ShippingAddressService;
import com.promesa.promesa.domain.shippingAddress.domain.ShippingAddress;
import com.promesa.promesa.domain.shippingAddress.dto.AddressResponse;
import com.promesa.promesa.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class ShippingAddressController {
    private final ShippingAddressService shippingAddressService;

    @GetMapping
    @Operation(summary = "기본 배송지 정보를 조회")
    public ResponseEntity<AddressResponse> getShippingAddress(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Member member = (user != null) ? user.getMember() : null;
        AddressResponse response =  shippingAddressService.getShippingAddress(member);
        return ResponseEntity.ok(response);
    }
}

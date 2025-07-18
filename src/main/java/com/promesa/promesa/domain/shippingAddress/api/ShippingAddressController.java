package com.promesa.promesa.domain.shippingAddress.api;

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
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @Operation(summary = "기본 배송지 정보를 추가")
    public ResponseEntity<AddressResponse> addShippingAddress(
            @RequestBody @Valid AddressRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ){
        Member member = (user != null) ? user.getMember() : null;
        AddressResponse response = shippingAddressService.addShippingAddress(request, member);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Operation(summary = "기본 배송지 정보를 삭제")
    public ResponseEntity<AddressResponse> deleteShippingAddress(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Member member = (user != null) ? user.getMember() : null;
        shippingAddressService.deleteShippingAddress(member);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    @Operation(summary = "기본 배송지 정보를 수정")
    public ResponseEntity<AddressResponse> updateShippingAddress(
            @RequestBody @Valid AddressRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    )
    {
        Member member = (user != null) ? user.getMember() : null;
        AddressResponse response = shippingAddressService.updateShippingAddress(request, member);
        return ResponseEntity.ok(response);
    }
}

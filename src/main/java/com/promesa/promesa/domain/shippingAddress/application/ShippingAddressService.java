package com.promesa.promesa.domain.shippingAddress.application;

import com.nimbusds.openid.connect.sdk.claims.Address;
import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.shippingAddress.dao.ShippingAddressRepository;
import com.promesa.promesa.domain.shippingAddress.domain.ShippingAddress;
import com.promesa.promesa.domain.shippingAddress.dto.request.AddressRequest;
import com.promesa.promesa.domain.shippingAddress.dto.response.AddressResponse;
import com.promesa.promesa.domain.shippingAddress.exception.ShippingAddressAlreadyExistsException;
import com.promesa.promesa.domain.shippingAddress.exception.ShippingAddressNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;
    private final MemberRepository memberRepository;

    /**
     * 기본 배송지 조회
     * @param member
     * @return
     */
    public AddressResponse getShippingAddress(Member member) {
        if (member.getShippingAddress() == null) {
            return null;
        }
        ShippingAddress address = member.getShippingAddress();
        return AddressResponse.from(address);
    }

    /**
     * 기본 배송지 추가
     * @param request
     * @param member
     * @return
     */
    @Transactional
    public AddressResponse addShippingAddress(@Valid AddressRequest request, Member member) {
        if (member.getShippingAddress() != null)    // 기본 배송지는 하나만 등록 가능
            throw ShippingAddressAlreadyExistsException.EXCEPTION;

        ShippingAddress address = ShippingAddress.builder()
                .recipientName(request.getRecipientName())
                .zipCode(request.getZipCode())
                .addressMain(request.getAddressMain())
                .addressDetails(request.getAddressDetails())
                .recipientPhone(request.getRecipientPhone())
                .build();
        ShippingAddress savedAddress = shippingAddressRepository.save(address);
        member.updateShippingAddress(savedAddress);
        memberRepository.save(member);
        return AddressResponse.from(savedAddress);
    }

    /**
     * 기본 배송지 정보 삭제
     * @param member
     */
    @Transactional
    public void deleteShippingAddress(Member member) {
        ShippingAddress address = member.getShippingAddress();
        member.updateShippingAddress(null);
        memberRepository.save(member);
        shippingAddressRepository.delete(address);
    }

    /**
     * 기본 배송지 정보 수정
     * @param request
     * @param member
     */
    @Transactional
    public AddressResponse updateShippingAddress(@Valid AddressRequest request, Member member) {
        ShippingAddress address = shippingAddressRepository.findById(member.getShippingAddress().getId())
                .orElseThrow(() -> ShippingAddressNotFoundException.EXCEPTION);

        address.update(
                request.getRecipientName(),
                request.getZipCode(),
                request.getAddressMain(),
                request.getAddressDetails(),
                request.getRecipientPhone()
        );

        return AddressResponse.from(address);
    }

    /**
     * 기본 배송지를 추가 또는 수정
     * @param request
     * @param member
     * @return
     */
    @Transactional
    public AddressResponse addOrUpdateShippingAddress(@Valid AddressRequest request, Member member) {
        AddressResponse response;
        if (member.getShippingAddress() != null) response = updateShippingAddress(request, member);
        else response = addShippingAddress(request, member);

        return response;
    }
}
